import {useMutation} from '@tanstack/react-query';
import API from '../../lib/api/axios';
import {useAuthStore} from '../../store/authStore';
import type {Role} from "../../lib/types/Role.ts";

interface RegisterPayload {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone: string;
    role: Role
}

interface AuthResponse {
    token: string;
}

export const useRegister = () => {
    const setToken = useAuthStore(state => state.setToken);

    return useMutation<AuthResponse, Error, RegisterPayload>(
        (userData) => API.post('/auth/register', userData).then(res => res.data),
        {
            onSuccess: (data) => {
                setToken(data.token);
            },
        }
    );
};