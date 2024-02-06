<template>
<nav id="header" class="w-full z-30 top-1 py-1 bg-white shadow-lg border-b border-green-400">
      <div class="w-full flex items-center justify-between mt-0 px-6 py-2">
         <div @click="toggleMenu" class="cursor-pointer md:hidden block">
            <svg class="fill-current text-green-500" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
               <title>menu</title>
               <path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"></path>
            </svg>
         </div>
        <Menu class="z-50"
        :top = menuTop 
        :left = menuLeft
        :margin = menuMargin
        @change-route="menu=false"
        v-show="menu" />
         <input class="hidden" type="checkbox" v-model="menu" id="menu-toggle">
         <div class="hidden md:flex md:items-center md:w-auto w-full order-3 md:order-1" id="menu">
            <nav>
               <ul class="md:flex items-center justify-between text-base text-blue-600 pt-4 md:pt-0">
                  <li><router-link to="/" class="inline-block no-underline hover:text-black font-medium text-lg py-2 px-4 lg:-ml-2">
                        Home
                       </router-link>
                  </li>
                  <li v-if="isLoggedIn">
                    <router-link to="/reserved" class="inline-block no-underline hover:text-black font-medium text-lg py-2 px-4 lg:-ml-2">
                      Area Personale
                    </router-link>
                  </li>
                  <li>
                    <router-link to="/about" class="inline-block no-underline hover:text-black font-medium text-lg py-2 px-4 lg:-ml-2">
                      Informazioni
                    </router-link>
                  </li>
               </ul>
            </nav>
         </div>
         
         <div class="order-2 md:order-3 flex flex-wrap items-center justify-end mr-0 md:mr-1" id="nav-content">
            <div class="auth flex items-center w-full md:w-full">
               <router-link v-if="!isLoggedIn" to="/login" class="bg-transparent text-gray-800  p-2 rounded border border-gray-300 mr-1 hover:bg-gray-100 hover:text-gray-700">
                Login
                </router-link>
               <router-link v-else to="/logout" class="bg-transparent text-gray-800  p-2 rounded border border-gray-300 mr-1 hover:bg-gray-100 hover:text-gray-700">
                Logout
                </router-link>
            </div>
         </div>
      </div>
</nav>

<Modal v-if="showModal">
  <!-- Modal Header -->
  <template v-if="modalTitle==''" v-slot:header>
    <h3 class="font-bold mb-2">Connessione assente</h3>
     <hr>
  </template>
  <template v-else v-slot:header>
    <h3 class="font-bold mb-2">{{modalTitle}}</h3>
     <hr>
  </template>

  <!-- Modal Body -->
  <template v-if="modalBody==''" v-slot:body>
    <span>Sembra che tu non sia connesso ad Internet...<br><br>
     Dopo aver ristabilito la connessione, ricarica questa pagina per continuare ad usare questo software.
    </span>
  </template>
   <template v-else v-slot:body>
    <span v-html="modalBody"></span>
  </template>
   
  <!-- Modal Footer -->
  <template v-if="!modalFooter"  v-slot:footer>
  </template>
  <template v-else  v-slot:footer>
     <button type="button" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1" @click="dismissModal">
           Ok
      </button>
  </template>
</Modal>

<router-view/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import Menu from '@/components/Menu.vue';
import Modal from '@/components/Modal.vue';
@Options({
 components: {
  Menu,
  Modal,
  }
})
export default class App extends Vue {
  [x: string]: any;
  menu = false;
  menuTop = 55;
  menuLeft = 0;
  menuMargin = 0;
  modalTitle = "";
  modalBody = "";
  modalFooter = false;
  showModal = false;
  mounted(){
    this.$store.commit('changeOnline',navigator.onLine);
    this.$store.commit('resetMessage');
    if(!this.status){
      this.showModal = true;
      return;
    }
    if(this.expiringToken){
      console.log("Token expired...");
      this.modalTitle = "Sessione scaduta";
      this.modalBody = "La tua sessione è scaduta! Effettua nuovamente il login...";
      this.showModal = true;
      this.$store.commit("logout");
    }
  }
  get isLoggedIn(){
    return this.$store.getters.isLoggedIn;
  }
  get status(): boolean{
      return this.$store.getters.isOnline;

  }
  get expiringToken(): boolean{
      return this.$store.getters.isTokenExpired;
  }
  @Watch('status')
  onStatusChanged(value: boolean, oldValue: boolean) {
      if(!value){
          this.showModal = true;
      }else{
          this.showModal = false;
      }
  }
  @Watch('expiringToken')
  onTokenExpired(value: boolean, oldValue: boolean) {
      if(value){
         console.log("Token expired...");
         this.modalTitle = "Sessione scaduta";
         this.modalBody = "La tua sessione è scaduta! Effettua nuovamente il login...";
         this.showModal = true;
         this.$store.commit("logout");
      }
  }

  get modalMessage(): string{
    return this.$store.getters.eventMessage;
  }
  
  @Watch('modalMessage')
  onMessageChanged(value: string, oldValue: string){
    if(!this.showModal){ //don't display any message if you're displaying something now
      if(value!=""){
        this.modalTitle = "Errore";
        this.modalBody = value;
        this.showModal = true;
      }
    }
  }

  @Watch('menu')
  onPropertyChanged(value: boolean, oldValue: boolean) {
    if(value){
      console.log("watched");
    }
  }
  toggleMenu(){
    if(this.menu){
      this.menu = false;
    }else{
      this.menu = true;
    }
  }
  dismissModal(){
     this.showModal = false;
     this.$store.commit('resetMessage');
     this.modalTitle = "";
     this.modalBody = "";
     
  }

  
}
</script>


<style>
#app {
  font-family: 'Quicksand',Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

a {
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}

a.router-link-exact-active{
   color: #42b983;
   background-color: #42b9832c ;
   border-radius: 10%;
}


</style>
