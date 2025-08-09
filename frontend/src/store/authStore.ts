import { create } from "zustand";
import { decodeToken, isTokenExpired } from "../lib/utils/jwt";
import type {Role} from "../lib/types/Role.ts";

interface User {
    email: string;
    role: Role;
}

interface AuthState {
    token: string | null;
    user: User | null;
    isAuthenticated: boolean;
    setToken: (token: string | null) => void;
    setUser: (user: User | null) => void; // <--- добавь сюда
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
                    role: decoded.role as Role,
                };

                set({ token, user, isAuthenticated: true });
            } else {
                get().logout();
            }
        } else {
            localStorage.removeItem('token');
            set({ token: null, user: null, isAuthenticated: false });
        }
    },

    setUser: (user) => {  // <--- реализуй
        set({ user });
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