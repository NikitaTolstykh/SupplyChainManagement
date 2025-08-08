import React from 'react';

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label?: string;
    error?: string;
}

const Input: React.FC<InputProps> = ({label, error, ...props}) => {
    return (
        <div className="mb-4">
            {label && <label className="block text-gray-700 mb-1">{label}</label> }
            <input
                className={`w-full border rounded py-2 px-3 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                    error ? 'border-red-500' : 'border-gray-300'
                }`}
                {...props}
            />
            {error && <p className="text-red-600 text-sm mt-1">{error}</p>}
        </div>

    );
};

export default Input;

