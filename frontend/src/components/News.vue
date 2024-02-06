<template >
<div id="newsWrapper" v-show="isVisible">
  <div class="md:flex md:content-center md:items-center mb-3" >
    <div style="text-align:center" class="md:flex-initial  overflow-hidden content-center newsImage md:w-screen md:h-auto">
      <a :href="newsDto.href" ><img :src="newsDto.enclosure" style="display:inline;" class=" mt-2" @error="imageLoadError" loading="lazy" ></a>
    </div>
    <div class="md:ml-4 md:flex-initial">
      <div id="newsTitle" class="lg:text-left" ><a :href="newsDto.href">{{newsDto.title}}</a></div>
      <div id="newsDescription" class="text-left" ><span v-html="newsDto.description"></span></div>
      <div id="newsSource" class="text-left mt-2">Fonte: {{newsDto.source}}</div> 
    </div>
  </div>
  <hr>
</div>
</template>




 <script lang="ts">
import { Options, Vue } from 'vue-class-component';
import { InjectReactive } from 'vue-property-decorator';
import NewsDTO from '@/DTO/NewsDTO';

@Options({
  props: {
    newsDto: Object,
  }
})

export default class News extends Vue {
    newsDto!: NewsDTO;
    @InjectReactive() selectedTag!: number
    get isVisible(): boolean {
       if(this.selectedTag == 0){
         return this.newsDto.topNews;
       }
       if(this.newsDto.tags == undefined){
         return false;
       }
       if(this.newsDto.tags == ""){
         return false;
       }
       const tagsDeserialzed: number[] = this.newsDto.tags.split(",").map(x=>+x);
       if (tagsDeserialzed.includes(this.selectedTag)){
         return true;
       }
       return false; 
    }
    imageLoadError(){
      //console.log("error detected");
      this.newsDto.enclosure = "./assets/logo.png";
    }
}

 </script>

 <style scoped>
#newsTitle {
  font-weight: bold;
}
#newsDescription { 
  font-size: small;
}
#newsSource { 
  font-size: small;
}
.hideNews{
  display: none;
}
@media (min-width: 768px) { 
 .newsImage{
   width: 200;
   height: auto;
   max-width: 200px;
   max-height: 150px;
 }

}


 </style>