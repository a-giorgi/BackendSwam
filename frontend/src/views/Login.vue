<template>
<div :style="style">
<div style="width:100%; text-align:center; " >
  <SignUpController @login="slider=1" v-if="slider==0" width="18rem" fromLeft="9rem"/>
  <LoginController @signup="slider=0" v-else-if="slider==1" width="18rem" fromLeft="9rem"/>
</div>
</div>



</template>

<script lang="ts">
import { mixins, Options } from 'vue-class-component';
import SignUpController from '@/controllers/SignUpController.vue';
import LoginController from '@/controllers/LoginController.vue';
import ResponsiveAdapter from '@/utils/ResponsiveAdapter';

@Options({
  components: {
    SignUpController,
    LoginController
  },
})
export default class Login extends mixins(ResponsiveAdapter) {
  slider = 1;
  get style(): string{
    return  "max-height: " + String(this.newHeight-75)+"px; overflow:auto;";  
  }   
}

</script>

<style scoped>
</style>