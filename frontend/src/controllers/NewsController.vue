<template>
<div :style="style" class="p-3 overflow-auto">
<News v-for="element in newsArray"
  :key="element.id"
  :newsDto="element"
></News>
  <div class="mt4 text-center content-center w-full" v-if="noNews">
    Nessuna notizia disponibile
  </div>
</div>
    
</template>

<script lang="ts">
import { Options, mixins } from 'vue-class-component';
import News from '@/components/News.vue';
import NewsDTO from '@/DTO/NewsDTO';
import NewsRestServices from '@/services/NewsRestServices';
import ResponsiveAdapter from '@/utils/ResponsiveAdapter';
import InternetServices from '@/services/InternetServices';


@Options({
  components: {
    News,
  }
})
export default class NewsController extends mixins(NewsRestServices,ResponsiveAdapter,InternetServices) {
    tag!: number;
    newsArray: NewsDTO[] = [];
    noNews = false;
    mounted(){
        this.fetchAllNews()
            .then(response => {
                this.newsArray = response.data.resource;
                if(this.newsArray.length<1){
                  this.noNews = true;
                }
            })
            .catch(error => {
              if(error.code === 'ECONNABORTED'){
                this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
              }else if(error.response.status == '503'){
                this.$store.commit('changeOnline',false);
              }
            });  
    }
    get style(): string{
      return  "max-height: " + String(this.newHeight-140)+"px;";  
    }   
}
</script>

<style scoped>
 .invisible{
   display: none;
 }


</style>