<template><hr>
<div class="font-bold mt-6 mb-3">
    <h4 >Amministratore di Sistema</h4>
</div>
<hr>
<div class="m-4">
  <h4>
    <a class="clickable" :class="{ clicked: isFeedSelected }" @click="slider=0">Gestisci Feed</a> | 
    <a class="clickable" :class="{ clicked: isTagSelected }"  @click="slider=1">Gestisci Tag</a>
  </h4>
</div>
<TagController v-if="slider==1"/>
<FeedController v-else-if="slider==0"/>
</template>

<script lang="ts">
import { Options, Vue} from 'vue-class-component';
import FeedController from '@/controllers/FeedController.vue';
import TagController from '@/controllers/TagController.vue'


//TODO ATTENZIONE A QUANDO DESELEZIONO TUTTI I TAG E GESTISCI COUNTRYCODE

@Options({
  components: {
    FeedController,
    TagController,
  }
})
export default class Administration extends Vue{
  slider = 0;
  get isTagSelected(): boolean{
    if(this.slider == 1){
      return true;
    }
    return false;
  }
  get isFeedSelected(): boolean{
    if(this.slider == 0){
      return true;
    }
    return false;
  }
  
}





</script>

<style scoped>

.clickable{
  font-weight: bold;
  text-decoration: underline;
  cursor:pointer;
  border-radius: 10%;
  padding:1%;
  padding-left: 2%;
  padding-right: 2%;
}

.clickable:hover{
  background: #42b9832d;
}

.clicked{
  color: #42b983;
  background-color: rgba(66, 185, 131, 0.089);
}
.clicked:hover{
  background:  rgba(66, 185, 131, 0.089);
}

.appear-enter-active,
.appear-active {
  transition: opacity 0.5s ease;
  transform: traslateX(30)
}

.appear-enter-from,
.appear-leave-to {
  transform: traslateX(30)
}


</style>