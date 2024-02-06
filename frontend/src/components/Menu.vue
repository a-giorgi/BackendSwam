<template>
    <div :style="style" class="absolute  mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
    <div class="py-1" role="menu" aria-orientation="vertical" aria-labelledby="options-menu">
      <router-link to="/" @click="$emit('change-route')" class="block px-4 py-2 text-sm text-green-700 hover:bg-green-100 hover:text-green-900" role="menuitem"> Home </router-link>
      <router-link v-if="isLoggedIn" to="/reserved" @click="$emit('change-route')" class="block px-4 py-2 text-sm text-green-700 hover:bg-green-100 hover:text-green-900" role="menuitem"> Area Personale </router-link>
      <router-link to="/about"  @click="$emit('change-route')" class="block px-4 py-2 text-sm text-green-700 hover:bg-green-100 hover:text-green-900" role="menuitem"> Informazioni </router-link>
    </div>
  </div> 
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";

@Options({
    props: {
        top: Number,
        left: Number,
        margin: Number,
  }
})
export default class Menu extends Vue{
    top!: number;
    left!: number;
    margin!: number;
    get style(): string{
        return "top:"+this.top+"px; left:"+this.left+"px; margin: "+this.margin+"px; z-index: 999;" ;
        
    }
    get isLoggedIn(){
    return this.$store.getters.isLoggedIn;
    }
}
</script>
