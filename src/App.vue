<template>
  <!-- <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link>
  </nav> -->
  <router-view v-slot="{Component}">
    <keep-alive>
	  	<component :is="Component"/>
	  </keep-alive>
  </router-view>
	
</template>

<style lang="less">

</style>
