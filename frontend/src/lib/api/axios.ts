import axios from "axios";

const API = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
    headers: {
        "Content-Type": "application/json",
    },
});

API.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token && !config.url?.includes('/auth/')) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default API;