import React, { useState } from 'react';
import Button from '../../components/ui/Button';
import { useRegister } from '../../hooks/auth/useRegister';
import { useNavigate } from 'react-router-dom';
import {useAuthStore} from "../../store/authStore.ts";
import { RoleValues } from "../../lib/types/Role.ts";

interface RegisterFormData {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone: string;
}


const RegisterPage: React.FC = () => {
    const [formData, setFormData] = useState<RegisterFormData>({
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        phone: '',
    });

    const registerMutation = useRegister();
    const navigate = useNavigate();
    const setToken = useAuthStore(state => state.setToken);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        registerMutation.mutate(formData, {
            onSuccess: (data) => {
                setToken(data.token);

                if (data.role === RoleValues.ADMIN) {
                    navigate('/admin');
                } else {
                    navigate('/');
                }
            },
            onError: (error) => alert(error.message),
        });
    };


    const fields: (keyof RegisterFormData)[] = ['firstName', 'lastName', 'email', 'phone', 'password'];

    return (
        <div className="min-h-screen bg-gradient-to-br from-purple-50 via-pink-50 to-orange-50 flex items-center justify-center">
            <form onSubmit={handleSubmit} className="max-w-md w-full bg-white p-8 rounded-2xl shadow-xl">
                <h2 className="text-3xl font-bold mb-8 text-center text-gray-900">Register</h2>

                {fields.map((field) => (
                    <div key={field} className="mb-5">
                        <label htmlFor={field} className="block mb-1 font-semibold capitalize">{field}</label>
                        <input
                            id={field}
                            name={field}
                            type={field === 'password' ? 'password' : field === 'email' ? 'email' : 'text'}
                            value={formData[field]}
                            onChange={handleChange}
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500"
                            required
                        />
                    </div>
                ))}

                <Button type="submit" variant="primary" className="w-full py-3" disabled={registerMutation.isPending}>
                    {registerMutation.isPending ? 'Registering...' : 'Register'}
                </Button>

                <p className="mt-6 text-center">
                    Already have an account?{' '}
                    <a href="/login" className="text-blue-600 hover:text-blue-700 font-medium">
                        Login
                    </a>
                </p>
            </form>
        </div>
    );
};

export default RegisterPage;
