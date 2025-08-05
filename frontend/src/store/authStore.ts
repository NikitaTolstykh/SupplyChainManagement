import { create } from "zustand";
import { decodeToken, isTokenExpired } from "../lib/utils/jwt";
import { Role } from "../lib/types/Role";

interface User {
    email: string;
    role: Role;
}

interface AuthState {
    token: string | null;
    user: User | null;
    isAuthenticated: boolean;
    setToken: (token: string | null) => void;
    logout: () => void;
    initializeAuth: () => void;
}

export const useAuthStore = create<AuthState>((set, get) => ({
    token: null,
    user: null,
    isAuthenticated: false,

    setToken: (token) => {
        if (token) {
            const decoded = decodeToken(token);
            if (decoded && !isTokenExpired(token)) {
                localStorage.setItem('token', token);
                const user: User = {
                    email: decoded.sub,
                    role: decoded.role as Role || Role.CLIENT
                };
                set({ token, user, isAuthenticated: true });
            } else {
                // Токен невалидный или истек
                get().logout();
            }
        } else {
            localStorage.removeItem('token');
            set({ token: null, user: null, isAuthenticated: false });
        }
    },

    logout: () => {
        localStorage.removeItem('token');
        set({ token: null, user: null, isAuthenticated: false });
    },

    initializeAuth: () => {
        const token = localStorage.getItem('token');
        if (token && !isTokenExpired(token)) {
            get().setToken(token);
        } else {
            get().logout();
        }
    },
}));