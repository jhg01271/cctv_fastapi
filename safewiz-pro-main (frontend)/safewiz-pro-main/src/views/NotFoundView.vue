<template>
  <h1>NotFound</h1>
  <div>404</div>
  <div>{{ $route.params.pathMatch }}</div>
  <button @click="$router.push('/')">Home으로</button>
</template>

<script setup></script>

<style lang="scss" scoped></style>
