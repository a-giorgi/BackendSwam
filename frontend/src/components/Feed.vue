<template>
<div :class="{hidden: hideByCountry}">
    <div class="mt-2"  style="text-align:left">
        <div><b>{{feedDto.name}}</b></div>
        <div>Id del feed: <b>{{feedDto.id}}</b></div>
        <div>URL: <b>{{feedDto.url}}</b></div>
        <div>Tags: <b>{{printTags}}</b></div>
        <div>Notizie relative a: <b>{{zoneExtended}}</b></div>
        <div v-if="feedDto.topNews" class="bg-green-600 mt-1 mb-1 whitespace-nowrap w-min text-white p-1 rounded  leading-none items-center" >
          Feed presente in prima pagina &check;
        </div>
        <div v-else  class="bg-gray-300 mb-1 whitespace-nowrap w-min text-white p-1 rounded  leading-none items-center">
           Feed nascosto in prima pagina 
        </div>
        <div v-if="feedDto.active" class="bg-green-600 whitespace-nowrap w-min text-white p-1 rounded  leading-none items-center"> Feed attivo &check; </div>
        <div v-else class="bg-gray-300 whitespace-nowrap w-min text-white p-1 rounded  leading-none items-center"> Feed non attivo </div> 

        <div v-if="isNotVisible"  class=" mt-2 underline text-red-500 font-semibold"> Attenzione: Il feed, nonostante sia attivo, non è visibile poiché tutti i tag assegnati non sono attivi e il feed non è presente in prima pagina </div>
    </div>
    <div class="mb-2" style="text-align:right">
        <button @click="$emit('edit',componentId)" 
        class="bg-green-500 inline font-bold text-white mr-2 mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
          Modifica
        </button>
        <button @click="$emit('delete',componentId)" 
        class="bg-green-500 inline font-bold text-white mt-1 px-2 py-1 transition duration-300 ease-in-out hover:bg-green-700 mr-1">
          Elimina
        </button>
    </div>
    <hr>
</div>
</template>

<script lang="ts">
import { Options, Vue,} from 'vue-class-component';
import FeedDTO from '@/DTO/FeedDTO';
import TagDTO from '@/DTO/TagDTO';


@Options({
  props: {
    feedDto: Object,
    tags: Array,
    componentId: Number,
    filterCountryCode: String,
  },
   emits: {
    delete: false,
    edit: false
  }
})



export default class Feed extends Vue{  
    feedDto!: FeedDTO;
    tags!: TagDTO[];
    componentId!: number;
    filterCountryCode!: string;
    mounted(){
      console.log("mounted "+this.feedDto.id);
      console.log(this.feedDto);
    }
    get isNotVisible(){
      if(this.feedDto.topNews == false){
        let found = true;
        this.tags.forEach(element => {
          if(element.active == true){
            found = false;
          }
        });
        return found
      }
      return false;
    }
    get zoneExtended(): string{
      let output = this.feedDto.countryCode;
      if(this.$store.getters.isZonesLoaded){
        this.$store.getters.getZones.forEach(element =>{
          if(element.countryCode == this.feedDto.countryCode){
            output = element.name + " ("+ element.countryCode +") ";
          }
        });
      }
      return output;
    }
    get printTags(): string{
      const defaultOutput = "Nessun tag specificato";
      let output = "";
      this.tags.forEach(element =>{
        output+= element.name + " ("+ element.id +"), "
      });
      if(output.length<1){
        return defaultOutput;
      }else{
        return output.substring(0,output.length-2);
      }
    }
    get hideByCountry(): boolean{
      if(this.filterCountryCode== undefined){
        return false;
      }
      if(this.filterCountryCode== "" || this.filterCountryCode=="ALL"){
        return false;
      }
      if(this.feedDto.countryCode!==this.filterCountryCode){
        return true;
      }
      return false;
    }
}
</script>