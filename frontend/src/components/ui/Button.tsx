import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: React.ReactNode;
    variant?: 'primary' | 'secondary' | 'success' | 'danger';
}

const variantClasses = {
    primary: 'bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors duration-200',
    secondary: 'bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium py-2 px-4 rounded-lg transition-colors',
    success: 'bg-emerald-600 hover:bg-emerald-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors',
    danger: 'bg-red-600 hover:bg-red-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors',
};

const Button: React.FC<ButtonProps> = ({children, variant = 'primary', className = "", ...props}) => {
    return (
        <button
            className={`${variantClasses[variant]} ${className}`}
            {...props}
        >
            {children}
        </button>
    );
};

export default Button;
