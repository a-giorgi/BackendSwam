<template>
<div class="font-bold mt-6 mb-3">
   <h4 v-if="isAdmin">Benvenuto Amministratore {{username}}</h4> 
   <h4 v-else>Benvenuto {{username}}</h4> 
</div>
<hr>
<div class="text-left pl-3 mt-2">
   
   <span> Username: <b>{{username}}</b></span> <br>
   <span> E-mail: <b> {{email}} </b></span> <br>
   <div v-if="savedZone == null" class="pb-2">
        <span >Non hai impostato una zona, le notizie saranno mostrate geolocalizzando il tuo indirizzo IP</span>
        <br>
        <button class="bg-green-500 font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1"
        @click="showModal = true"> Imposta Zona </button>
        <button  class="bg-green-500 font-bold text-white mt-1 ml-2 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700"
        @click="showPasswordModal = true">Cambia Password</button>
   </div>
   <div v-else class="pb-2">
        <span >Hai impostato la zona {{savedZone.name}} ({{savedZone.countryCode}}), pertanto saranno mostrate le notizie a questa relative</span>
        <br>
        <button class="bg-green-500 font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1"
         @click="showModal = true"> Cambia Zona </button>
        <button class="bg-green-500 font-bold text-white mt-1 ml-2 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700"
        @click="showPasswordModal = true">Cambia Password </button>
   </div>
</div>
<hr>
  <button class="bg-green-500 font-bold text-white m-2 px-3 py-2 transition duration-300 ease-in-out hover:bg-green-700 mr-6"
v-if="isAdmin" @click="this.$router.push('/admin')">Apri area di amministrazione</button>

 <Modal v-if="showModal" @close="dismissModal" @send="submitModal">
        <!-- Conditional Modal Header -->
        <template v-if="savedZone == null" v-slot:header>
          <h3 class="font-bold mb-2">Imposta Zona</h3>
          <hr>
        </template>
        <template  v-else v-slot:header>
            <h3 class="font-bold mb-2">Cambia Zona</h3>
        </template>

        <!-- Conditional Modal Body -->
        <template  v-slot:body>
          <div class="text-center content-center">
           <label for="country" class="mb-1">Seleziona la tua zona</label><br>
            <select id="country" class="appearance-none pl-2 border border-green-500 rounded "  v-model="countryCode">
              <option v-for="zone in zones" :key="zone.countryCode" :selected="isCountrySelected(zone)" :value="zone.countryCode">{{zone.name}} ({{zone.countryCode}})</option>
            </select><br>
             <div id="output"  :class="{ hidden: errorMessage=='', 'inline-block': errorMessage!=''}"  class="mt-2 p-1 bg-red-500 w-min whitespace-nowrap text-white font-bold rounded">{{errorMessage}}</div>
          </div>
        </template>


        <!-- Conditional Modal Footer -->
        <template v-slot:footer><hr>
        <div class="mt-2 ">
            <button class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1"
             type="submit">Salva</button>
            <div  v-if="savedZone != null" class="inline">
              <button type="button" class="bg-green-500 font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1" @click="remove">
                Rimuovi
              </button>
            </div>
            <button type="button" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1" @click="dismissModal">
              Annulla
            </button>
          </div>
        </template>

 </Modal>

  <Modal v-if="showPasswordModal" @close="dismissPasswordModal" @send="submitPasswordModal">
        <!-- Modal Header -->
        <template v-slot:header>
            <h3 class="font-bold mb-2">Cambia Password</h3>
        </template>

        <!-- Conditional Modal Body -->
        <template v-if="!passwordSuccess" v-slot:body>
         <label for="oldPassword">Password:</label><br>
         <input id="oldPassword" type="password" v-model="oldPassword" maxlength="25" class="rounded border w-full p-1 text-gray-800 border-green-200 bg-white mb-2" placeholder="(Scrivi qui la tua password attuale)"><br>
         <label for="newPassword">Nuova Password:</label><br>
         <input id="newPassword" type="password" v-model="newPassword" maxlength="25" class="rounded border w-full p-1 text-gray-800 border-green-200 bg-white mb-2" placeholder="(Scrivi qui la tua nuova password)"><br>
         <label for="confirmPassword">Conferma Nuova Password:</label><br>
         <input id="confirmPassword" type="password" v-model="confirmPassword" maxlength="25" class="rounded border w-full p-1 text-gray-800 border-green-200 bg-white mb-2" placeholder="(Conferma qui la tua nuova password)"><br>
        <div class="w-full text-center content-center">
          <div id="output"  :class="{ hidden: passwordErrorMessage=='', 'inline-block': passwordErrorMessage!=''}"  class="mt-2 p-1 bg-red-500  text-white font-bold rounded">
            {{passwordErrorMessage}}
          </div>
        </div>
        </template>
        <template v-else v-slot:body>
           <div class="text-center w-full content-center">
             Password modificata con successo!
           </div>
        </template>

        <!-- Conditional Modal Footer -->
        <template v-if="!passwordSuccess" v-slot:footer><hr>
        <div class="mt-2 ">
            <button class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1"
             type="submit">Cambia Password</button>
            <button type="button" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1" @click="dismissPasswordModal">
              Annulla
            </button>
          </div>
        </template>
        <template v-else v-slot:footer>
        </template>

  </Modal>

</template>

<script lang="ts">
import { Options, mixins } from 'vue-class-component';
import TokenDTO  from '@/DTO/TokenDTO';
import ZoneDTO from '@/DTO/ZoneDTO';
import Modal from '@/components/Modal.vue'
import ZoneRestServices from '@/services/ZoneRestServices';
import UserRestServices from '@/services/UserRestServices';
import UserDTO from '@/DTO/UserDTO';
import InternetServices from '@/services/InternetServices';


@Options({
 components: {
   Modal
  }
})
export default class UserController extends mixins( ZoneRestServices, UserRestServices, InternetServices ){
    username = "";
    isAdmin = false;
    email = "";
    countryCode = "";
    countryName = "";
    savedZone: ZoneDTO|null = null;
    userId: number|null = null;
    zones: ZoneDTO[] = [];
    showModal = false;
    errorMessage = "";

    //fields for the password modal
    showPasswordModal = false;
    oldPassword = "";
    newPassword = "";
    confirmPassword = "";
    passwordErrorMessage = "";
    passwordSuccess = false;

    mounted(){
        const token = this.$store.getters.getToken;
        if(token != null){
            const tokens = token!.split(".");
            const tokenInfo: TokenDTO = JSON.parse(atob(tokens[1]));
            this.username = tokenInfo.username;
            this.email = tokenInfo.email;
            this.isAdmin = tokenInfo.isAdmin;
            this.userId = tokenInfo.userId;
        }
        if(this.userId!=null){
          this.getUserInfo(this.userId!).then(response =>{
              const userDto = response.data.resource as UserDTO;
              if(userDto.countryCode != undefined){
                this.countryCode = userDto.countryCode;
              }
              if (this.$store.getters.isZonesLoaded){
                  this.zones = this.$store.getters.getZones;
                  this.initUserZone();
              }else{
                  //retrieving zones
                  this.fetchAllZones()
                  .then(response => {
                       this.$store.commit('storeZones',response.data.resource as ZoneDTO[]);
                       console.log(this.$store.getters.getZones);
                       this.zones = response.data.resource as ZoneDTO[];
                       this.initUserZone();

                  }).catch(error =>{
                      console.log(error);
                      if(this.verifyOnline()){
                        this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
                      }
                  });
              }         
          }).catch(error =>{
            console.log(error);
            if(this.verifyOnline()){
              this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
            }
        });
        }
    }  
    dismissModal(){
        this.showModal = false;
        this.errorMessage = "";
    }
    dismissPasswordModal(){
        this.showPasswordModal = false;
        this.passwordSuccess = false;
        this.passwordErrorMessage = "";
        this.oldPassword = "";
        this.newPassword = "";
        this.confirmPassword = "";
    }
    submitModal(){
        if(this.countryCode==""){
          //alert("Seleziona una zona!");
          this.errorMessage = "Seleziona una zona!"
          return;
        }
        this.submitCountryCode();
        this.dismissModal();
    }
    remove(){
      this.countryCode="";
      this.submitCountryCode();
      this.dismissModal();
    }
    submitPasswordModal(){
      if(this.oldPassword==""){
        this.passwordErrorMessage="Inserisci la tua password attuale!";
        return;
      }
      const isPasswordVerified: string | boolean = this.passwordVerify(this.newPassword,this.confirmPassword);
      if(isPasswordVerified!=true){
        this.passwordErrorMessage = isPasswordVerified.toString();
        return;
      }
      if(this.newPassword == this.oldPassword){
        this.passwordErrorMessage="La nuova password deve essere diversa da quella attuale!";
        return;
      }
      const userDto: UserDTO = {
            "id": this.userId,
            "username": "",
            "email": "",
            "password": this.newPassword,
            "confirmPassword": this.confirmPassword,
            "oldPassword": this.oldPassword,
            "countryCode": ""
      }
      this.resetUserPassword(userDto)
          .then(response => {
              console.log(response);
              this.passwordSuccess = true;
              setTimeout(() => { this.dismissPasswordModal() }, 1500);
          })
          .catch(error => {
          if(error.code === 'ECONNABORTED'){
              this.dismissPasswordModal();
              this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
              return;
           }
            this.passwordErrorMessage = this.decodeUserResponseError(error.response.data.code as string, error.response.status as number);  
          })


    }
    submitCountryCode(){
      const userDto: UserDTO = {
            "id": this.userId,
            "username": "",
            "email": "",
            "password": "",
            "confirmPassword": "",
            "oldPassword": "",
            "countryCode": this.countryCode
      }
      this.saveUser(userDto)
          .then(response => {
            console.log(response);
            if(this.countryCode==""){
              this.savedZone = null;
            }else{
              this.savedZone = {
                name: this.countryName,
                countryCode: this.countryCode
              }
            }
           })
          .catch(error => {
          if(error.code === 'ECONNABORTED'){
              this.dismissPasswordModal();
              this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
              return;
           }
            this.errorMessage = this.decodeUserResponseError(error.response.data.code as string, error.response.status as number);  
          })
    }
    //countryCode related method
    isCountrySelected(zone: ZoneDTO): boolean{
      if(this.countryCode == zone.countryCode && this.countryCode!== ""){
        this.countryName = zone.name;
        return true;
      }
      return false;
    }

    findUserCountryName(countryCode): string{
       let found = "";
       this.zones.forEach(element =>{
         if(element.countryCode == countryCode){
           found = element.name;
         }
       });
       return found;

    }

    initUserZone(){
      if(this.countryCode!=""){
          this.countryName = this.findUserCountryName(this.countryCode);
          this.savedZone = { 
            name: this.countryName,
            countryCode: this.countryCode
          }
      } 
    }
}
</script>