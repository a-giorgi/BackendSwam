<template>
<div style="width:100%; text-align:center;" >
 <span>Logout in corso...</span>
</div>



</template>

<script lang="ts">
import { Vue } from 'vue-class-component';

export default class Logout extends Vue {
  [x: string]: any;
  mounted(){
      this.$store.commit('logout');
      this.$router.push('/');
  }
}

</script>

<style scoped>
</style>