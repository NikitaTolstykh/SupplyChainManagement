import React from "react";
import { Routes, Route } from 'react-router-dom';
import LoginPage from "../pages/auth/LoginPage.tsx";

const AppRoutes= () => {
    return(
        <Routes>
            <Route path="/login" element={<LoginPage />} />        </Routes>
    );
};

export default AppRoutes