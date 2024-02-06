<template>
<hr>
  <div class="reserved">
    <UserController />
  </div>
  <hr>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import UserController from '@/controllers/UserController.vue';

@Options({
  components: {
    UserController,
  },
})
export default class Reserved extends Vue {

}
</script>
