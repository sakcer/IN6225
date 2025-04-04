import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/userStore'

// Define admin routes
const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/admin/dashboard',
    meta: { requiresAdmin: true }, // Require admin access
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: 'Dashboard', icon: 'Monitor' } // Dashboard title and icon
      },
      {
        path: 'employees',
        name: 'EmployeeList',
        component: () => import('@/views/employees/EmployeeList.vue'),
        meta: { title: 'Employees', icon: 'User' } // Employee management title and icon
      },
      {
        path: 'projects',
        name: 'ProjectList',
        component: () => import('@/views/projects/ProjectList.vue'),
        meta: { title: 'Projects', icon: 'Folder' } // Project management title and icon
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: 'Profile', icon: 'User' } // Personal information title and icon
      },
    ]
  }
]

// Define employee routes
const employeeRoutes = [
  {
    path: '/employee',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/employee/dashboard',
    meta: { requiresEmployee: true }, // Require employee access
    children: [
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: () => import('@/views/user/Dashboard.vue'),
        meta: { title: 'User Panel', icon: 'Monitor' } // User panel title and icon
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: 'Profile', icon: 'User' } // Personal information title and icon
      }
    ]
  }
]

// Create the router instance
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/common/Login.vue'),
      meta: { title: 'Login' } // Login title
    },
    ...adminRoutes, // Spread admin routes
    ...employeeRoutes, // Spread employee routes
    {
      path: '/:pathMatch(.*)*', // Catch-all route for 404
      name: 'NotFound',
      component: () => import('@/views/common/NotFound.vue'),
      meta: { title: '404' } // 404 title
    }
  ]
})

// Navigation guard
router.beforeEach(async (to, from, next) => {
  console.log(to, from);
  const userStore = useUserStore();

  const isLoggedIn = await userStore.isLoggedIn();
  const isAdmin = userStore.isAdmin;
  console.log('isLoggedIn:', isLoggedIn);

  if (!isLoggedIn && to.path !== '/login') {
    return next({ path: '/login' });
  }

  if (to.meta['requiresAdmin'] && !isAdmin) {
    console.log('Admin access required');
    return next({ path: '/NotFound' });
  }

  if (to.meta['requiresEmployee'] && isAdmin) {
    console.log('Employee access required');
    return next({ path: '/NotFound' });
  }

  if (to.path === '/' || to.path === '/login' && isLoggedIn) {
    console.log('Redirecting based on role');
    return next({ path: isAdmin ? '/admin/dashboard' : '/employee/dashboard' });
  }

  next();
});

// Export the router and routes
export default router 
export { adminRoutes, employeeRoutes };