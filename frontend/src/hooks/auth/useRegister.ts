import { useMutation } from '@tanstack/react-query';
import API from '../../lib/api/axios';
import { useAuthStore } from '../../store/authStore';

interface RegisterPayload {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone: string;
}

interface AuthResponse {
    token: string;
    type: string; // "Bearer"
}

export const useRegister = () => {
    const setToken = useAuthStore((state) => state.setToken);

    return useMutation({
        mutationFn: (userData: RegisterPayload) =>
            API.post('/auth/register', { ...userData, role: 'CLIENT' }).then((res) => res.data),
        onSuccess: (data: AuthResponse) => {
            setToken(data.token);
        },
    });
};