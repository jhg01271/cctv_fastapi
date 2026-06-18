import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'


const HOST = 'localhost'
const API_URL = `http://${HOST}:9000`
const IGNS_AUTH_URL = `http://${HOST}:9000`
import svgLoader from 'vite-svg-loader';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    svgLoader(),
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  optimizeDeps: {
    include: ['overlayscrollbars', 'overlayscrollbars-vue']
  },
  server: {
    port: 3000,
    host: true,
    proxy: {
      "^/igns/auth": {
        target: IGNS_AUTH_URL,
        changeOrigin: true,        
        secure: false,
        ws: true,
        onProxyReq: (proxyReq, req) => {
          proxyReq.setHeader('X-Real-IP', req.ip);
        }
      },
      "^/admin": {
          target: API_URL,
          changeOrigin: true,
          secure: false,
          ws: true,
          onProxyReq: (proxyReq, req) => {
              proxyReq.setHeader('X-Real-IP', req.ip);
          }
      },
      "^/base": {
          target: API_URL,
          changeOrigin: true,
          secure: false,
          ws: true,
          onProxyReq: (proxyReq, req) => {
              proxyReq.setHeader('X-Real-IP', req.ip);
          }
      },
      "^/comp": {
          target: API_URL,
          changeOrigin: true,
          secure: false,
          ws: true,
          onProxyReq: (proxyReq, req) => {
              proxyReq.setHeader('X-Real-IP', req.ip);
          }
      },
      "^/fems": {
          target: API_URL,
          changeOrigin: true,
          secure: false,
          ws: true,
          onProxyReq: (proxyReq, req) => {
              proxyReq.setHeader('X-Real-IP', req.ip);
          }
      },
      "^/system": {
          target: API_URL,
          changeOrigin: true,
          secure: false,
          ws: true,
          onProxyReq: (proxyReq, req) => {
              proxyReq.setHeader('X-Real-IP', req.ip);
          }
      },
      "^/safewizpro": {
        target: API_URL,
        changeOrigin: true,
        secure: false,
        ws: true,
        onProxyReq: (proxyReq, req) => {
          proxyReq.setHeader('X-Real-IP', req.ip);
        }
      },
      '/cctv': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        secure: false,        
        ws: true
      },      
      "^/business": {
          target: API_URL,
          changeOrigin: true,
          secure: false,
          ws: true,
          onProxyReq: (proxyReq, req) => {
              proxyReq.setHeader('X-Real-IP', req.ip);
          }
      },     
    }
  }
})
