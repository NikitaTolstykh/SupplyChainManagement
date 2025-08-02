import { useMutation } from "@tanstack/react-query";
import API from "../../lib/api/axios.ts";
import { useAuthStore } from "../../store/authStore.ts";

interface LoginPayload {
    email: string;
    password: string;
}

interface AuthResponse {
    token: string;
}

export const useLogin = () => {
    const setToken = useAuthStore(state => state.setToken);

    return useMutation<AuthResponse, Error, LoginPayload>(
        ({ email, password }) =>
            API.post('/auth/login', { email, password }).then(res => res.data),
        {
            onSuccess: (data) => {
                setToken(data.token);
            },
        }
    );
};
