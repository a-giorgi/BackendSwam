<template>
<div>
    <hr>
    <button @click="showNewModal" class="bg-green-500 inline font-bold text-white mt-2 mb-2 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1" style="float-right">
      Aggiungi Feed
      </button>
    <hr>
</div>
<div class="m-2">
     Filtra per zona:
      <select class="appearance-none p-1 border border-green-200 rounded " v-model="filterCountryCode">
            <option value="ALL">Mostra tutti i feed</option>
            <option v-for="zone in zones"  :key="zone.countryCode" :value="zone.countryCode">{{zone.name}} ({{zone.countryCode}})</option>
      </select>
</div>
<hr>
    <div class="p-3" :style="style">
    <Feed 
    v-for="(element, index) in feedArray"
    :key= element.id
    :feedDto= element
    :tags="deserializeTags(element.tags)"
    :componentId="index"
    :filterCountryCode="filterCountryCode"
    @edit="showEditModal"
    @delete="showDeleteModal" />
    </div>
    <Modal v-if="showModal" @close="dismissModal" @send="submitModal">
        <!-- Conditional Modal Header -->
        <template v-if="newFeedModal" v-slot:header>
           <h3 class="font-bold mb-2">Aggiungi feed</h3>
           <hr>
        </template>
        <template v-else-if="editFeedModal" v-slot:header>
           <h3 class="font-bold mb-2">Modifica feed</h3>
           <hr>
        </template>
         <template v-else-if="deleteFeedModal" v-slot:header>
          <h3 class="font-bold mb-2">Elimina feed</h3>
          <hr>
        </template>

        <!-- Conditional Modal Body -->
        <template v-if="newFeedModal || editFeedModal" v-slot:body>
          

            <label for="name">Nome del Feed:</label><br>
            <input type="text" v-model="feedName" maxlength="60" class="rounded border w-full p-1 text-gray-800 border-green-200 bg-white mb-2"><br>
            <label v-show="newFeedModal" for="url">Url del Feed:</label><br>
            <input v-show="newFeedModal" type="text" v-model="feedURL" maxlength="200" class="rounded border w-full p-1 text-gray-800 border-green-200 bg-white mb-2" placeholder="(Scrivi qui l'url del Feed)"><br>
            <span>Seleziona Tags:</span><br>
            <div class="mb-3" style="max-height:100px; overflow:auto">
              <div style="display:inline-flex;" class="justify-start items-start mr-2" v-for="element in numericTagArray" :key=element.id>
              <div class="bg-white border-2 rounded border-gray-400 w-5 h-5  flex-shrink-0 justify-center items-center mr-2 focus-within:border-green-500">
                <input type="checkbox"  @click="toggleTag(element.id)" :checked="isTagSelected(element.id)"  class=" cursor-pointer opacity-0 absolute">
                <svg class="fill-current hidden w-4 h-4 text-green-500 pointer-events-none" viewBox="0 0 20 20"><path d="M0 11l2-2 5 5L18 3l2 2L7 18z"/></svg>
              </div>
              <span class="select-none">{{element.name}}</span>&nbsp; |
              </div>
            </div> 
            <label for="country">Seleziona la zona del feed</label><br>
            <select id="country" class="appearance-none w-full p-1 border border-green-200 rounded " v-model="countryCode">
              <option v-for="zone in zones" :key="zone.countryCode" :selected="isCountrySelected(zone.countryCode)" :value="zone.countryCode">{{zone.name}} ({{zone.countryCode}})</option>
            </select><br>

             <label class="mt-4 flex justify-start items-start">
              <div class="bg-white border-2 rounded border-gray-400 w-5 h-5  flex-shrink-0 justify-center items-center mr-2 focus-within:border-green-500">
                <input type="checkbox"  v-model="topNews" class=" cursor-pointer opacity-0 absolute">
                <svg class="fill-current hidden w-4 h-4 text-green-500 pointer-events-none" viewBox="0 0 20 20"><path d="M0 11l2-2 5 5L18 3l2 2L7 18z"/></svg>
              </div>
              <span class="select-none">Mostra in prima pagina</span>
              </label>

             <label class="mt-2 flex justify-start items-start">
              <div class="bg-white border-2 rounded border-gray-400 w-5 h-5  flex-shrink-0 justify-center items-center mr-2 focus-within:border-green-500">
                <input type="checkbox" v-model="active" class=" cursor-pointer opacity-0 absolute">
                <svg class="fill-current hidden w-4 h-4 text-green-500 pointer-events-none" viewBox="0 0 20 20"><path d="M0 11l2-2 5 5L18 3l2 2L7 18z"/></svg>
              </div>
              <span class="select-none">Feed Attivo</span>
              </label><br>
              <div class="text-center content-center w-full">
                <div id="output"  :class="{ hidden: errorMessage=='', 'inline-block': errorMessage!=''}"  class="mt-1 p-1 bg-red-500 w-min whitespace-nowrap text-white font-bold rounded">{{errorMessage}}</div>
              </div>

            
          
        </template>
         <template v-else-if="deleteFeedModal" v-slot:body>
          <span>Questa operazione &egrave; irreversibile, vuoi procedere?</span>
        </template>

        <!-- Conditional Modal Footer -->
        <template v-if="newFeedModal" v-slot:footer>
          <hr>
          <div class="mt-2">
            <button type="submit" class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
              Crea Nuovo Feed
            </button>
            <button type="button" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1" @click="dismissModal">
              Annulla
            </button>
          </div>
        </template>
        <template v-else-if="editFeedModal" v-slot:footer>
          <hr>
          <div class="mt-2">
            <button type="submit" class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
              Modifica Feed
            </button>
            <button type="button" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1" @click="dismissModal">
              Annulla
            </button>
          </div>
        </template>
         <template v-else-if="deleteFeedModal" v-slot:footer>
           <hr>
           <div class="mt-2">
         <button type="submit" class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
           Elimina Feed
         </button>
         <button type="button" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1" @click="dismissModal">
           Annulla
          </button>
           </div>
        </template>

    </Modal>

   
</template>

<script lang="ts">
import { Options,  mixins } from 'vue-class-component';
import Feed from '@/components/Feed.vue';
import Modal from '@/components/Modal.vue'
import FeedDTO from '@/DTO/FeedDTO';
import TagDTO from '@/DTO/TagDTO'
import ZoneDTO from '@/DTO/ZoneDTO'
import TagRestServices from '@/services/TagRestServices';
import FeedRestServices from '@/services/FeedRestServices';
import ZoneRestServices from '@/services/ZoneRestServices';
import NewsRestServices from '@/services/NewsRestServices';
import ResponsiveAdapter from '@/utils/ResponsiveAdapter';
import InternetServices from '@/services/InternetServices';

interface TagDtoMap{
  [index: string]: TagDTO;
}
//TODO ATTENZIONE A QUANDO DESELEZIONO TUTTI I TAG E GESTISCI COUNTRYCODE

@Options({
  components: {
    Feed,
    Modal
  }
})
export default class FeedController extends mixins(
   TagRestServices,
   FeedRestServices, 
   ZoneRestServices, 
   NewsRestServices, 
   ResponsiveAdapter,
   InternetServices ) {
  [x: string]: any;
    tag!: number;
    //retrieved feeds stay here
    feedArray: FeedDTO[] = [];
    //tagDtoMap: for the index it uses the tag id
    tagDtoMap: TagDtoMap = {};
    //the following array is required for the directive "v-for"
    numericTagArray: TagDTO[] = [];
    //retrieved zones stay here
    zones: ZoneDTO[] = [];
    //method required to display and customize the modal
    showModal = false;
    newFeedModal = false;
    editFeedModal = false;
    deleteFeedModal = false;
    //attributes required to handle new/edit feed
    active!: boolean;
    feedName!: string;
    feedURL!: string;
    topNews!: boolean;
    arrayFeedId: number | null = null;    
    feedId: number | null = null;
    selectedTagIds: number[] = [];
    countryCode!: string;
    filterCountryCode = "ALL";
    errorMessage = "";
    //required to detect modification and clear the cache
    feedsModified = false;
    mounted() {
        //retrieving tags
        this.fetchAllTags()
        .then(response => {
            this.numericTagArray = response.data.resource;
            this.numericTagArray.forEach(element => {
              this.tagDtoMap[element.id!.toString()] = element;
            });
             //retrieving feeds
            this.fetchAllFeeds()
            .then(response =>{
                this.feedArray = response.data.resource as FeedDTO[];
                if (this.$store.getters.isZonesLoaded){
                    this.zones = this.$store.getters.getZones;
                    
                }else{
                    //retrieving zones
                    this.fetchAllZones()
                    .then(response => {
                         this.$store.commit('storeZones',response.data.resource as ZoneDTO[]);
                         this.zones = response.data.resource as ZoneDTO[];
                    })
                    .catch(error =>{
                        console.log(error);
                        if(this.verifyOnline()){
                          this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
                        }
                    });
                }
            })
            .catch(error => {
                console.log(error);
                if(this.verifyOnline()){
                  this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
                }
            });
            
        })
        .catch(error => {
              console.log(error);
              if(this.verifyOnline()){   
                  this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
              }
        });
    
    }

    unmounted() {
      if(this.feedsModified){
        this.deleteNewsCache();
      }
    }

    get style(): string{
      return  "max-height: " + String(this.newHeight-290)+"px; overflow:auto;";  
    }

    
    
    //showing Modals
    showDeleteModal (componentId: number){
      this.feedId = this.feedArray[componentId].id;
      this.deleteFeedModal = true;
      this.arrayFeedId = componentId;
      this.showModal = true;
    }
    showEditModal(componentId: number){
      this.feedId = this.feedArray[componentId].id;
      this.feedName = this.feedArray[componentId].name;
      this.feedURL = this.feedArray[componentId].url;
      this.topNews = this.feedArray[componentId].topNews;
      this.active = this.feedArray[componentId].active;
      this.countryCode = this.feedArray[componentId].countryCode;
      this.arrayFeedId = componentId;
      this.editFeedModal = true;
      this.showModal = true;
      this.selectedTagIds = [];
      this.selectTags(this.feedArray[componentId].tags);
    }
    showNewModal(){
      this.newFeedModal = true;
      this.showModal = true;
      this.feedId = null;
      this.arrayFeedId  = null;
      this.feedName = "";
      this.feedURL = "";
      this.countryCode = "IT";
      this.topNews = false;
      this.active = false;
      this.selectedTagIds = [];

    }
    dismissModal(){
      this.showModal = false;
      this.errorMessage = "";
      this.newFeedModal = false;
      this.editFeedModal = false;
      this.deleteFeedModal = false;
    }

    //submitting modal
    submitModal(){
      console.log("Submitting modal...");
      if(this.deleteFeedModal){
        this.submitDeleteFeed();
      }
      if(this.feedName==""){
        this.errorMessage = "Specifica il nome del feed!"
        return;
      }
      if(this.countryCode==""){
        this.errorMessage = "Seleziona una zona!"
        return;
      }
      if(this.newFeedModal){
        if(this.feedURL==""){
          this.errorMessage = "Specifica l'URL del feed!"
          return;
        }
        this.submitNewFeed();
      }
      else if(this.editFeedModal){
        this.submitEditFeed();
      }else{
        console.log("unsupported operation!");
      }
    }

    //submitting to backend using mixins
    submitNewFeed(){
      console.log("SubmitNewFeed")
      const feedDto: FeedDTO = {
          id: this.feedId,
          active: this.active,
          countryCode: this.countryCode,
          name: this.feedName,
          tags: this.rollTag(this.selectedTagIds),
          topNews: this.topNews,
          url: this.feedURL
      }
      this.saveFeed(feedDto).then(response => {
           this.feedsModified = true;
           const updated = response.data.resource as FeedDTO;
           feedDto.id = updated.id!;
           this.feedArray.push(feedDto);
           this.dismissModal();
      }).catch(error =>{
        this.manageSubmitErrors(error)
      });
    }
    submitEditFeed(){
      console.log("SubmitEditFeed");
      const feedDto: FeedDTO = {
          id: this.feedArray[this.arrayFeedId!].id,
          active: this.active,
          countryCode: this.countryCode,
          name: this.feedName,
          tags: this.rollTag(this.selectedTagIds),
          topNews: this.topNews,
          url: this.feedArray[this.arrayFeedId!].url
      }
      this.saveFeed(feedDto)
      .then(response =>{
          this.feedsModified = true;
          this.feedArray[this.arrayFeedId!] = response.data.resource as FeedDTO;     
          this.dismissModal();
      }).catch(error => {
          this.manageSubmitErrors(error);
      }) 
    }  
    submitDeleteFeed(){
      this.deleteFeed(this.feedId!)
      .then(response =>{ 
          this.feedsModified = true;
          console.log(response);
          this.feedArray.splice(this.arrayFeedId!,1);
          this.dismissModal();
        })
      .catch(error => {
        this.manageSubmitErrors(error);
        });
    }

    deserializeTags(commaSeparatedTags: string): TagDTO[]{
        const output: TagDTO[] = [];
        if(commaSeparatedTags == undefined){
          return output;
        }
        if(commaSeparatedTags == ""){
          return output;
        }
        const tagIds = commaSeparatedTags.split(',');
        tagIds.forEach(element => {
          const tagDto: TagDTO = {
            id: +element,
            name: this.tagDtoMap[element].name,
            active: this.tagDtoMap[element].active,
          }
          output.push(tagDto);
        });
        return output;
    }

    //Tag Modal methods
    toggleTag(id: number){
      if(this.selectedTagIds.includes(id)){
        const index = this.selectedTagIds.indexOf(id);
        if (index > -1) {
          this.selectedTagIds.splice(index, 1);
        }
      }else{
        this.selectedTagIds.push(id);
      }
    }

    selectTags(commaSeparatedTags: string){
       if(commaSeparatedTags == undefined || commaSeparatedTags == ""){
         return;
       }
       const tagIds = commaSeparatedTags.split(',');
       tagIds.forEach(element => {
          this.toggleTag(+element);
       });
    }

    isTagSelected(id: number): boolean{
       return this.selectedTagIds.includes(id);
    }

    rollTag(tagsIds: number[]): string{
      let commaSeparatedTags = "";
      tagsIds.forEach(element =>{
        commaSeparatedTags += element+",";
      });
      if(commaSeparatedTags.length>1){
        return commaSeparatedTags.substr(0,commaSeparatedTags.length-1);
      }
      return "";

    }

    //countryCode related method
    isCountrySelected(countryCode: string): boolean{
      if(this.countryCode == countryCode){
        return true;
      }
      return false;
    }

    manageSubmitErrors(error){
      console.log(error);
      if(this.verifyOnline()){
          if(error.code !== 'ECONNABORTED'){
            this.errorMessage = this.decodeFeedResponseError(error.response.data.code as string, error.response.status as number);
          }else{
            this.dismissModal();
            this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
          }
      }
    }
}

</script>

<style scoped>
  input:checked + svg {
    display: block;
  }
</style>