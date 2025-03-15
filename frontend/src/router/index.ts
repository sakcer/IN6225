import { createRouter, createWebHistory } from 'vue-router'
import { useMeStore } from '@/store/meStore'
import { USER_ROLES } from '@/utils/constants'

const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/admin/dashboard',
    meta: { requiresAdmin: true },
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
      {
        path: 'projects',
        name: 'ProjectList',
        component: () => import('@/views/projects/ProjectList.vue'),
        meta: { title: '项目管理', icon: 'Folder' }
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: '个人信息', icon: 'User' }
      },
    //   {
    //     path: 'user',
    //     name: 'UserList',
    //     component: () => import('@/views/user/Dashboard.vue'),
    //     meta: { title: '用户管理', icon: 'User' }
    //   },
    ]
  }
]

const employeeRoutes = [
  {
    path: '/employee',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/employee/dashboard',
    meta: {requiresEmployee: true},
    children: [
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: () => import('@/views/user/Dashboard.vue'),
        meta: { title: '员工面板', icon: 'Monitor' }
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
    //   redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/common/Login.vue'),
      meta: { title: '登录' }
    },
    ...adminRoutes,
    ...employeeRoutes,
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('../views/common/NotFound.vue'),
      meta: { title: '404' }
    }
  ]
})

router.beforeEach((to, from, next) => {
  const meStore = useMeStore();
  const { role, token } = meStore;

  const isLogin = !!token;
  const isAdmin = role === USER_ROLES.ADMIN;
  console.log(role, token);

  if (!isLogin && to.path !== '/login') {
    console.log('Redirecting to login');
    return next({ path: '/login' });
  }

  if (to.meta.requiresAdmin && !isAdmin) {
    console.log('Admin access required');
    return next({ path: '/NotFound' });
  }

  if (to.meta.requiresEmployee && isAdmin) {
    console.log('Employee access required');
    return next({ path: '/NotFound' });
  }

  if (to.path === '/') {
    console.log('Redirecting based on role');
    return next({ path: isAdmin ? '/admin/dashboard' : '/employee/dashboard' });
  }

  console.log('Proceeding to', to.path);
  next();
});

export default router 
export { adminRoutes, employeeRoutes };