import { ElMessage } from 'element-plus';
import axios from 'axios';

export const handleAxiosError = (error: unknown): void => {
    if (error === 'cancel') {
        return;
    }
    if (axios.isAxiosError(error)) {
        const errorMessage = error.response?.data?.message || 'An unexpected error occurred';
        ElMessage.error(errorMessage);
    } else {
        ElMessage.error('An unexpected error occurred');
    }

    console.error(error);
};
