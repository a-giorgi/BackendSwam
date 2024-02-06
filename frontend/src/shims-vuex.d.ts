import { ComponentCustomProperties } from 'vue';
import { Store } from 'vuex';
import ZoneDTO from '@/DTO/ZoneDTO';

declare module '@vue/runtime-core' {
  // declare your own store states
  interface State {
    token: string | null;
    isTokenExpired: boolean;
    zones: ZoneDTO[];
    online: boolean;
    message: string;
  }

  // provide typings for `this.$store`
  interface ComponentCustomProperties {
    $store: Store<State>;
  }
}

// Vuex@4.0.0-beta.1 is missing the typing for `useStore`. See https://github.com/vuejs/vuex/issues/1736
declare module 'vuex' {
  export function useStore(key?: string): Store<State>;
}