import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')  
    }
  },
  server: {
    proxy: {
      // '/api': {
      //     // target: 'http://127.0.0.1:4523/m1/5965061-5653226-default', 
      //     target: 'http://127.0.0.1:8080/api/v1', 
      //     changeOrigin: true,
      //     rewrite: (path) => path.replace(/^\/api/, '')
      // }
    }
  },
})
