import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../layouts/DefaultLayout.vue'),
      children: [
        {
          path: '',
          name: 'Dashboard',
          component: () => import('../views/Dashboard.vue')
        },
        {
          path: 'employees',
          name: 'EmployeeList',
          component: () => import('../views/employees/EmployeeList.vue')
        },
        {
          path: 'employees/add',
          name: 'AddEmployee',
          component: () => import('../views/employees/EmployeeForm.vue')
        },
        {
          path: 'employees/edit/:id',
          name: 'EditEmployee',
          component: () => import('../views/employees/EmployeeForm.vue')
        },
        // {
        //   path: 'departments',
        //   name: 'DepartmentList',
        //   component: () => import('../views/departments/DepartmentList.vue')
        // }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue')
    }
  ]
})

export default router 