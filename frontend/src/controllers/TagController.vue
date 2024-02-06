<template>
    <div>
       <hr>
        <button @click="showNewModal" :style="style" class="bg-green-500 inline font-bold text-white mt-2 mb-2 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
            Aggiungi Tag
        </button>
        <hr>
    </div>
    <div  class="p-3" :style="style">
        <Tag 
        v-for="(element, index) in numericTags"
        :key = element.id
        :tagDto = element
        :componentId = index
        @edit="showEditModal"
        @delete="showDeleteModal" />
    </div>

    <Modal v-if="showModal" @close="dismissModal" @send="submitModal">
        <!-- Conditional Modal Header -->
        <template v-if="newTagModal" v-slot:header>
           <h3 class="font-bold mb-2">Aggiungi Tag</h3>
           <hr>
        </template>
        <template v-else-if="editTagModal" v-slot:header>
          <h3 class="font-bold mb-2">Modifica Tag</h3>
           <hr>
        </template>
         <template v-else-if="deleteTagModal" v-slot:header>
          <h3 class="font-bold mb-2">Elimina Tag</h3>
           <hr>
        </template>

        <!-- Conditional Modal Body -->
        <template v-if="newTagModal || editTagModal" v-slot:body>
          

            <label for="name">Nome del Tag:</label><br>
            <input type="text" v-model="tagName" maxlength="60" :disabled="editTagModal" :class="styleClasses"><br>
            <label class="mt-2 flex justify-start items-start">
            <div class="bg-white border-2 rounded border-gray-400 w-5 h-5  flex-shrink-0 justify-center items-center mr-2 focus-within:border-green-500">
              <input type="checkbox" v-model="active" class=" cursor-pointer opacity-0 absolute">
              <svg class="fill-current hidden w-4 h-4 text-green-500 pointer-events-none" viewBox="0 0 20 20"><path d="M0 11l2-2 5 5L18 3l2 2L7 18z"/></svg>
            </div>
            <span class="select-none">Tag Attivo</span>
            </label><br>
            <div class="text-center content-center w-full">
                <div id="output"  :class="{ hidden: errorMessage=='', 'inline-block': errorMessage!=''}"  class="mt-1 p-1 bg-red-500 w-min whitespace-nowrap text-white font-bold rounded">{{errorMessage}}</div>
            </div>
          
        </template>
         <template v-else-if="deleteTagModal" v-slot:body>
          <span>Questa operazione &egrave; irreversibile, vuoi procedere?</span>
        </template>

        <!-- Conditional Modal Footer -->
        <template v-if="newTagModal" v-slot:footer>
            <button type="submit" class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
                Crea Nuovo Tag
            </button>
            <button type="button" @click="dismissModal" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1">
                Annulla
            </button>
        </template>
        <template v-else-if="editTagModal" v-slot:footer>
            <button type="submit" class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
                Modifica Tag
            </button>
             <button type="button" @click="dismissModal" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1">
                Annulla
            </button>
        </template>
         <template v-else-if="deleteTagModal" v-slot:footer>
         <button type="submit" class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
             Elimina Tag
         </button>
         <button type="button" @click="dismissModal" class="bg-gray-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-gray-700 mr-1">
             Annulla
         </button>
        </template>

    </Modal>



</template>

<script lang="ts">
import { Options,  mixins } from 'vue-class-component';
import Tag from '@/components/Tag.vue';
import Modal from '@/components/Modal.vue'
import TagDTO from '@/DTO/TagDTO'
import TagRestServices from '@/services/TagRestServices';
import ResponsiveAdapter from '@/utils/ResponsiveAdapter';
import InternetServices from '@/services/InternetServices';

@Options({
  components: {
    Modal,
    Tag
  }
})
export default class TagController extends mixins(TagRestServices, ResponsiveAdapter, InternetServices){
    private numericTags: TagDTO[] = [];
    //method required to display and customize the modal
    showModal = false;
    newTagModal = false;
    editTagModal = false;
    deleteTagModal = false;
    componentIndex: number | null = null;
    //attributes required to handle new/edit tag
    active = true;
    tagName = "";  
    tagId: number | null = null;
    errorMessage = "";
    mounted(){
        this.fetchAllTags().then(response => {
            this.numericTags = response.data.resource as TagDTO[];
        }).catch(error =>{
            console.log(error);
            if(this.verifyOnline()){
              this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
            }
        });
        //this.$store.commit('changeOnline',false);
    }

    //modal related methods
    showEditModal(componentId: number){
        this.active = this.numericTags[componentId].active;
        this.tagName = this.numericTags[componentId].name;
        this.tagId = this.numericTags[componentId].id;
        this.componentIndex = componentId;
        this.newTagModal = false;
        this.editTagModal = true;
        this.deleteTagModal = false;
        this.showModal = true;
    }

    showNewModal(){
        this.active = true;
        this.tagName = "";
        this.tagId = null;
        this.componentIndex = null;
        this.newTagModal = true;
        this.editTagModal = false;
        this.deleteTagModal = false;
        this.showModal = true;
    }

    showDeleteModal(componentId: number){
        this.tagId = this.numericTags[componentId].id;
        this.componentIndex = componentId;
        this.newTagModal = false;
        this.editTagModal = false;
        this.deleteTagModal = true;
        this.showModal = true;
    }

    dismissModal(){
        this.active = true;
        this.tagName = "";
        this.tagId = null;
        this.componentIndex = null;
        this.newTagModal = false;
        this.editTagModal = false;
        this.deleteTagModal = false;
        this.showModal = false;
    }

    submitModal(){
        const tagDto: TagDTO = {
            id: this.tagId,
            name: this.tagName,
            active: this.active
        } 
        if(!this.deleteTagModal){
            this.saveTag(tagDto).then(response => {
                console.log("Tag salvato con successo");
                const updated = response.data.resource as TagDTO;
                tagDto.id = updated.id!;
                if(this.componentIndex!=null){
                    this.numericTags[this.componentIndex] = tagDto;
                }else{
                    this.numericTags.push(tagDto);
                }
            })
            .catch(error => {
                this.manageSubmitErrors(error);
                this.dismissModal();
            });
        }else{
            this.deleteTag(this.tagId!).then(response =>{
                console.log("Tag eliminato con successo");
                console.log(response);
                this.numericTags.splice(this.componentIndex!,1);
            }).catch(error => {
                this.manageSubmitErrors(error);
                this.dismissModal();
            });
        }
        this.showModal = false;
    }

    get styleClasses(){
        if(!this.editTagModal){
            return "rounded border w-full p-1 text-gray-800 border-green-200 bg-white mb-2";
        }else{
            return "rounded border w-full p-1 text-gray-500 border-gray-300 bg-white mb-2 bg-gray-100";
        }
    }

    get style(): string{
      return  "max-height: " + String(this.newHeight-240)+"px; overflow:auto;";  
    }

    manageSubmitErrors(error){
      console.log(error);
      if(this.verifyOnline()){
          if(error.code !== 'ECONNABORTED'){
            this.errorMessage = this.decodeTagResponseError(error.response.data.code as string, error.response.status as number);
          }else{
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