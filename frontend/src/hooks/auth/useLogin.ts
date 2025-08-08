import { useMutation } from '@tanstack/react-query';
import API from '../../lib/api/axios';
import { useAuthStore } from '../../store/authStore';
import type {Role} from "../../lib/types/Role.ts";

interface LoginPayload {
    email: string;
    password: string;
}

interface AuthResponse {
    token: string;
    type: string; // "Bearer"
    role: Role;
}

export const useLogin = () => {
    const setToken = useAuthStore((state) => state.setToken);

    return useMutation({
        mutationFn: ({ email, password }: LoginPayload) =>
            API.post('/auth/login', { email, password }).then((res) => res.data),
        onSuccess: (data: AuthResponse) => {
            setToken(data.token);
        },
    });
};