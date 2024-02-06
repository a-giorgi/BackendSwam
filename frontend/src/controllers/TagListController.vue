<template>
    <ul id="tagList" >
        <li @click="$emit('selected',element.id)" v-for="element in tags" :key="element.id">
        <a>{{element.name}}</a>
        </li>
    </ul>
</template>

<script lang="ts">

//import { defineComponent } from 'vue';
import { mixins, Options } from 'vue-class-component';
import TagDTO from '@/DTO/TagDTO';
import TagRestServices from '@/services/TagRestServices';
import InternetServices from '@/services/InternetServices';

@Options({
  emits: {
    selected: false,
  }
})

export default class TagListController extends mixins(TagRestServices,InternetServices) {
    tags: TagDTO[] = [];
    mounted() {
      const topNews: TagDTO = {
        id: 0,
        name: "Prima Pagina",
        active: true,
      }
      this.tags.push(topNews);
      this.fetchActiveTags().then(response => {
            //console.log(response)
            const resources = response.data.resource as TagDTO[];
            resources.forEach( (element: TagDTO) => {
            this.tags.push(element)
            });
      }).catch(error =>{
        if(this.verifyOnline()){
           this.$store.commit("sendMessage","Il backend sembra essere offline...<br> Riprova più tardi.");
           console.log(error);
        }
      });
    }
  
        

}

</script>

<style scoped>
ul {
  align-items: center;
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333333;
  overflow-x: auto;
  white-space: nowrap;
  text-align: left;
}

li {
   display: inline-block;
   padding-top: 16px;
   padding-bottom: 16px;
   padding-left: 8px;
   padding-right: 8px;
}

li a {
 
  color: white;
  text-align: center;
  padding: 16px;
  text-decoration: none;
  cursor:pointer;
  white-space: nowrap;
}

li a:hover {
  background-color: #111111;
}

</style>