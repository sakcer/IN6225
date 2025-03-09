import { ref, markRaw } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { employeeService } from '@/services/employees/employeeService';
import { USER_STATUS } from '@/utils/constants';
import type { Employee, Statistic, PaginationConfig } from '@/utils/types';
import { User, UserFilled, Promotion, TrendCharts } from '@element-plus/icons-vue';

export function useEmployees() {
  // 状态管理
  const users = ref<Employee[]>([]);
  const filteredUsers = ref<Employee[]>([]);
  const statistics = ref<Statistic[]>([]);
  const pagination = ref<PaginationConfig>({ currentPage: 1, pageSize: 1, total: 0 });
  const loading = ref(false);
  const searchQuery = ref('');
  const status = ref(USER_STATUS.ACTIVE);
  const dialogVisible = ref(false);
  const isEdit = ref(false);
  const form = ref<Employee>({} as Employee);

  // 过滤用户
  function filterUsers(users: Employee[]) {
    const { currentPage, pageSize } = pagination.value;
    const lowercasedSearchQuery = searchQuery.value.toLowerCase();

    const filtered = users.filter(user => {
      const matchesStatus = status.value === 'all' || user.status === status.value;
      const matchesSearch = user.name.toLowerCase().includes(lowercasedSearchQuery) ||
        user.email.toLowerCase().includes(lowercasedSearchQuery) ||
        user.employeeId.toLowerCase().includes(lowercasedSearchQuery);
      return matchesStatus && matchesSearch;
    });

    return filtered.slice((currentPage - 1) * pageSize, currentPage * pageSize);
  }

  // 获取用户列表
  async function updateUsers() {
    try {
      const { employees } = await employeeService.getAllEmployees();
      statistics.value = [
        { label: 'Total Users', value: employees.length, icon: markRaw(User), type: 'primary' },
        { label: 'Active Users', value: employees.filter(u => u.status === 'active').length, icon: markRaw(UserFilled), type: 'success' },
        { label: 'New Users', value: employees.filter(u => u.status === 'new').length, icon: markRaw(Promotion), type: 'warning' },
        { label: 'Growth Rate', value: 0, icon: markRaw(TrendCharts), type: 'danger' }
      ];
      updateTotal(employees.length);
      filteredUsers.value = filterUsers(employees);
    } catch (error) {
      ElMessage.error('获取用户列表失败');
      console.error(error);
    } finally {
      loading.value = false;
    }
  }

  // 删除用户
  async function handleDelete(user: Employee) {
    try {
      await ElMessageBox.confirm(`确定要删除用户 ${user.name} 吗？`, '警告', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' });
      await employeeService.deleteEmployee(user.id!);
      ElMessage.success('删除成功');
      await updateUsers();
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('删除失败');
        console.error(error);
      }
    }
  }

  // 处理搜索
  function handleSearch() {
    updateUsers();
  }

  // 添加用户
  function handleAddUser() {
    isEdit.value = false;
    dialogVisible.value = true;
    form.value = {} as Employee;
  }

  // 编辑用户
  function handleEdit(user: Employee) {
    isEdit.value = true;
    dialogVisible.value = true;
    form.value = user;
  }

  // 提交表单
  function handleSubmit() {
    ElMessage.success('提交成功');
  }

  // 更新总数
  function updateTotal(total: number) {
    pagination.value.total = total;
  }

  // 重置分页
  function resetPagination() {
    pagination.value.currentPage = 1;
  }

  // 处理页码变化
  function handleCurrentChange(page: number) {
    pagination.value.currentPage = page;
  }

  // 处理每页条数变化
  function handleSizeChange(size: number) {
    pagination.value.pageSize = size;
    resetPagination();
    updateUsers();
  }

  return {
    // 状态
    form,
    isEdit,
    status,
    loading,
    statistics,
    searchQuery,
    filteredUsers,
    dialogVisible,
    pagination,

    // 方法
    updateUsers,
    handleAddUser,
    handleEdit,
    handleDelete,
    handleSubmit,
    handleSearch,
    handleCurrentChange,
    handleSizeChange,
  };
} 