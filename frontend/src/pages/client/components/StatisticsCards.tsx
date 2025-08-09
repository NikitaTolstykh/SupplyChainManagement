import React from "react";
import type {ClientStatisticsDto} from "../../../lib/types/ClientDtos.ts";

interface StatisticsCardsProps {
    stats: ClientStatisticsDto;
}

const StatisticsCards: React.FC<StatisticsCardsProps> = ({stats}) => {
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-white p-6 rounded-xl shadow">
                <h3 className="text-lg font-semibold mb-2">Total Orders</h3>
                <p className="text-3xl font-bold text-blue-600">{stats.totalOrders}</p>
            </div>
            <div className="bg-white p-6 rounded-xl shadow">
                <h3 className="text-lg font-semibold mb-2">Completed Orders</h3>
                <p className="text-3xl font-bold text-blue-600">{stats.completedOrders}</p>
            </div>
            <div className="bg-white p-6 rounded-xl shadow">
                <h3 className="text-lg font-semibold mb-2">Average Rating</h3>
                <p className="text-3xl font-bold text-yellow-400">{stats.averageRating.toFixed(1)}</p>
            </div>
        </div>
    );
};

export default StatisticsCards;