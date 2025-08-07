import React, {useEffect} from 'react';
import { BrowserRouter} from "react-router-dom";
import AppRoutes from './routes/AppRoutes'
import {QueryClientProvider} from "@tanstack/react-query";
import {useAuthStore} from "./store/authStore.ts";
import {queryClient} from "./lib/api/client.ts";

const App: React.FC = () => {
    const initializeAuth = useAuthStore((state) => state.initializeAuth);

    useEffect(() => {
        initializeAuth();
    }, [initializeAuth]);

    return (
        <QueryClientProvider client={queryClient}>
            <BrowserRouter>
                <AppRoutes />
            </BrowserRouter>
        </QueryClientProvider>
    );
};

export default App;