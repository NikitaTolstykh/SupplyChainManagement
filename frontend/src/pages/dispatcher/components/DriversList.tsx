import React from 'react';
import { useAvailableDrivers } from '../../../hooks/dispatcher/useAvailableDrivers';

const DriversList: React.FC = () => {
    const { data: drivers, isLoading, isError } = useAvailableDrivers();

    if (isLoading) {
        return (
            <div className="bg-white rounded-lg shadow p-6">
                <h3 className="text-lg font-semibold mb-4">Available Drivers</h3>
                <div className="space-y-3">
                    {[...Array(3)].map((_, i) => (
                        <div key={i} className="animate-pulse">
                            <div className="h-4 bg-gray-200 rounded w-3/4 mb-2"></div>
                            <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                        </div>
                    ))}
                </div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="bg-white rounded-lg shadow p-6">
                <h3 className="text-lg font-semibold mb-4">Available Drivers</h3>
                <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                    <p className="text-red-800">Error loading drivers</p>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between mb-4">
                <h3 className="text-lg font-semibold">Available Drivers</h3>
                <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full text-sm font-medium">
                    {drivers?.length || 0} available
                </span>
            </div>

            {!drivers || drivers.length === 0 ? (
                <div className="text-center py-8">
                    <div className="text-gray-400 text-4xl mb-2">🚗</div>
                    <p className="text-gray-600">No drivers available</p>
                </div>
            ) : (
                <div className="space-y-3 max-h-96 overflow-y-auto">
                    {drivers.map((driver) => (
                        <div
                            key={driver.id}
                            className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors"
                        >
                            <div className="flex items-center justify-between">
                                <div className="flex-1">
                                    <div className="flex items-center space-x-2 mb-1">
                                        <span className="w-3 h-3 bg-green-400 rounded-full"></span>
                                        <h4 className="font-semibold text-gray-900">
                                            {driver.firstName} {driver.lastName}
                                        </h4>
                                    </div>

                                    <div className="text-sm text-gray-600 space-y-1">
                                        <div className="flex items-center">
                                            <span className="mr-2">📞</span>
                                            {driver.phone}
                                        </div>
                                        <div className="flex items-center">
                                            <span className="mr-2">✉️</span>
                                            {driver.email}
                                        </div>
                                    </div>
                                </div>

                                <div className="text-right text-sm">
                                    <div className="font-medium text-gray-900 mb-1">
                                        {driver.vehicleBrand} {driver.vehicleModel}
                                    </div>
                                    <div className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs font-medium">
                                        {driver.licensePlate}
                                    </div>
                                </div>
                            </div>

                            <div className="mt-3 pt-3 border-t border-gray-200">
                                <div className="flex items-center justify-between text-xs text-gray-500">
                                    <span>Status: Ready</span>
                                    <span>Vehicle Ready</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default DriversList;