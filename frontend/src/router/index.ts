import { createRouter, createWebHistory } from 'vue-router'

// 管理员路由
const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '控制台', icon: 'Monitor' }
      },
      {
        path: 'employees',
        name: 'EmployeeList',
        component: () => import('@/views/employees/EmployeeList.vue'),
        meta: { title: '员工管理', icon: 'User' }
      },
    //   {
    //     path: 'departments',
    //     name: 'DepartmentList',
    //     component: () => import('@/views/departments/DepartmentList.vue'),
    //     meta: { title: '部门管理', icon: 'OfficeBuilding' }
    //   },
      {
        path: 'projects',
        name: 'ProjectList',
        component: () => import('@/views/projects/ProjectList.vue'),
        meta: { title: '项目管理', icon: 'Folder' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: '个人信息', icon: 'User' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/common/Login.vue'),
      meta: { title: '登录' }
    },
    ...adminRoutes,
    // ...employeeRoutes,
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('../views/common/NotFound.vue'),
      meta: { title: '404' }
    }
  ]
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    const isLogin = !!token;

    if (to.path !== '/login' && !isLogin) {
        next({ path: '/login' })
    } else {
        next()
    }
})

export default router 
export { adminRoutes };