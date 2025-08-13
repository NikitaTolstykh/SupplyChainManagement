import React, { useState } from 'react';
import { useOrderRatings } from '../../hooks/dispatcher/useDispatcherOrders';
import type { OrderRatingResponseDto } from '../../lib/types/DispatcherDtos';

const RatingsPage: React.FC = () => {
    const [sortBy, setSortBy] = useState<'date' | 'stars' | 'order'>('date');
    const [filterStars, setFilterStars] = useState<number | 'ALL'>('ALL');
    const [searchQuery, setSearchQuery] = useState('');

    const { data: ratings, isLoading, isError } = useOrderRatings();

    const filteredAndSortedRatings = React.useMemo(() => {
        if (!ratings) return [];

        let filtered = ratings.filter(rating => {
            const matchesStars = filterStars === 'ALL' || rating.stars === filterStars;
            const matchesSearch = !searchQuery ||
                rating.clientFullName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                rating.orderId.toString().includes(searchQuery) ||
                (rating.comment && rating.comment.toLowerCase().includes(searchQuery.toLowerCase()));

            return matchesStars && matchesSearch;
        });

        return filtered.sort((a, b) => {
            switch (sortBy) {
                case 'date':
                    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
                case 'stars':
                    return b.stars - a.stars;
                case 'order':
                    return b.orderId - a.orderId;
                default:
                    return 0;
            }
        });
    }, [ratings, sortBy, filterStars, searchQuery]);

    const getStarsDisplay = (stars: number) => {
        return '⭐'.repeat(stars) + '☆'.repeat(5 - stars);
    };

    const getStarsColor = (stars: number) => {
        if (stars >= 4) return 'text-green-600';
        if (stars >= 3) return 'text-yellow-600';
        return 'text-red-600';
    };

    const averageRating = React.useMemo(() => {
        if (!ratings || ratings.length === 0) return 0;
        return ratings.reduce((sum, rating) => sum + rating.stars, 0) / ratings.length;
    }, [ratings]);

    const ratingDistribution = React.useMemo(() => {
        if (!ratings) return {};
        const distribution = { 1: 0, 2: 0, 3: 0, 4: 0, 5: 0 };
        ratings.forEach(rating => {
            distribution[rating.stars as keyof typeof distribution]++;
        });
        return distribution;
    }, [ratings]);

    if (isLoading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="text-gray-600">Loading ratings...</div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="bg-red-50 border border-red-200 rounded-md p-4">
                <div className="text-red-800">Failed to load ratings.</div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div>
                <h1 className="text-2xl font-semibold text-gray-900">Order Ratings & Reviews</h1>
                <p className="text-gray-600">Monitor customer feedback and service quality</p>
            </div>

            {/* Statistics Cards */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="flex-1">
                            <p className="text-sm font-medium text-gray-600">Total Reviews</p>
                            <p className="text-3xl font-bold text-gray-900">{ratings?.length || 0}</p>
                        </div>
                        <div className="p-3 bg-blue-100 rounded-full">
                            <span className="text-blue-600 text-xl">📝</span>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="flex-1">
                            <p className="text-sm font-medium text-gray-600">Average Rating</p>
                            <p className="text-3xl font-bold text-yellow-500">
                                {averageRating > 0 ? averageRating.toFixed(1) : 'N/A'}
                            </p>
                        </div>
                        <div className="p-3 bg-yellow-100 rounded-full">
                            <span className="text-yellow-600 text-xl">⭐</span>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="flex-1">
                            <p className="text-sm font-medium text-gray-600">5-Star Reviews</p>
                            <p className="text-3xl font-bold text-green-600">
                                {ratingDistribution[5] || 0}
                            </p>
                        </div>
                        <div className="p-3 bg-green-100 rounded-full">
                            <span className="text-green-600 text-xl">🌟</span>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <div className="flex items-center">
                        <div className="flex-1">
                            <p className="text-sm font-medium text-gray-600">1-Star Reviews</p>
                            <p className="text-3xl font-bold text-red-600">
                                {ratingDistribution[1] || 0}
                            </p>
                        </div>
                        <div className="p-3 bg-red-100 rounded-full">
                            <span className="text-red-600 text-xl">⚠️</span>
                        </div>
                    </div>
                </div>
            </div>

            {/* Rating Distribution */}
            <div className="bg-white rounded-lg shadow p-6">
                <h3 className="text-lg font-semibold mb-4">Rating Distribution</h3>
                <div className="space-y-2">
                    {[5, 4, 3, 2, 1].map(stars => {
                        const count = ratingDistribution[stars as keyof typeof ratingDistribution] || 0;
                        const percentage = ratings ? (count / ratings.length) * 100 : 0;

                        return (
                            <div key={stars} className="flex items-center">
                                <div className="flex items-center w-20">
                                    <span className="text-sm">{stars}</span>
                                    <span className="ml-1 text-yellow-400">⭐</span>
                                </div>
                                <div className="flex-1 mx-4">
                                    <div className="bg-gray-200 rounded-full h-2">
                                        <div
                                            className="bg-yellow-400 h-2 rounded-full"
                                            style={{ width: `${percentage}%` }}
                                        />
                                    </div>
                                </div>
                                <div className="w-12 text-right text-sm text-gray-600">
                                    {count}
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>

            {/* Filters */}
            <div className="bg-white rounded-lg shadow-sm border p-4">
                <div className="flex flex-wrap gap-4">
                    <div className="flex-1 min-w-64">
                        <input
                            type="text"
                            placeholder="Search by client name, order ID, or comment..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                        />
                    </div>
                    <select
                        value={filterStars}
                        onChange={(e) => setFilterStars(e.target.value === 'ALL' ? 'ALL' : parseInt(e.target.value))}
                        className="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                        <option value="ALL">All Ratings</option>
                        <option value="5">5 Stars</option>
                        <option value="4">4 Stars</option>
                        <option value="3">3 Stars</option>
                        <option value="2">2 Stars</option>
                        <option value="1">1 Star</option>
                    </select>
                    <select
                        value={sortBy}
                        onChange={(e) => setSortBy(e.target.value as 'date' | 'stars' | 'order')}
                        className="border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    >
                        <option value="date">Sort by Date</option>
                        <option value="stars">Sort by Rating</option>
                        <option value="order">Sort by Order ID</option>
                    </select>
                </div>
                <div className="mt-3 text-sm text-gray-600">
                    Showing {filteredAndSortedRatings.length} of {ratings?.length || 0} reviews
                </div>
            </div>

            {/* Reviews List */}
            <div className="bg-white rounded-lg shadow">
                {filteredAndSortedRatings.length === 0 ? (
                    <div className="text-center py-12">
                        <div className="text-gray-400 text-4xl mb-4">⭐</div>
                        <h3 className="text-lg font-medium text-gray-900 mb-2">No reviews found</h3>
                        <p className="text-gray-600">
                            {searchQuery || filterStars !== 'ALL'
                                ? 'Try adjusting your filters to see more reviews.'
                                : 'Reviews will appear here once customers start rating their orders.'
                            }
                        </p>
                    </div>
                ) : (
                    <div className="divide-y divide-gray-200">
                        {filteredAndSortedRatings.map((rating) => (
                            <div key={rating.id} className="p-6">
                                <div className="flex items-start justify-between">
                                    <div className="flex-1">
                                        <div className="flex items-center space-x-3 mb-2">
                                            <h4 className="text-lg font-semibold text-gray-900">
                                                {rating.clientFullName}
                                            </h4>
                                            <div className="flex items-center">
                                                <span className={`text-lg ${getStarsColor(rating.stars)}`}>
                                                    {getStarsDisplay(rating.stars)}
                                                </span>
                                                <span className="ml-2 text-sm text-gray-600">
                                                    ({rating.stars}/5)
                                                </span>
                                            </div>
                                        </div>

                                        <div className="text-sm text-gray-600 mb-3">
                                            Order #{rating.orderId} • {new Date(rating.createdAt).toLocaleDateString()}
                                        </div>

                                        {rating.comment && (
                                            <div className="bg-gray-50 rounded-lg p-4">
                                                <p className="text-gray-700 leading-relaxed">
                                                    "{rating.comment}"
                                                </p>
                                            </div>
                                        )}

                                        {!rating.comment && (
                                            <div className="text-gray-500 italic">
                                                No comment provided
                                            </div>
                                        )}
                                    </div>

                                    <div className="ml-4 text-right">
                                        <div className={`text-2xl font-bold ${getStarsColor(rating.stars)}`}>
                                            {rating.stars}
                                        </div>
                                        <div className="text-xs text-gray-500">
                                            stars
                                        </div>
                                    </div>
                                </div>

                                {/* Actions */}
                                <div className="mt-4 pt-4 border-t border-gray-200">
                                    <div className="flex items-center justify-between">
                                        <div className="text-xs text-gray-500">
                                            Review ID: #{rating.id}
                                        </div>
                                        <div className="flex space-x-2">
                                            {rating.stars <= 2 && (
                                                <span className="bg-red-100 text-red-800 px-2 py-1 rounded-full text-xs font-medium">
                                                    Needs Attention
                                                </span>
                                            )}
                                            {rating.stars >= 4 && (
                                                <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs font-medium">
                                                    Positive Feedback
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* Insights */}
            {ratings && ratings.length > 0 && (
                <div className="bg-white rounded-lg shadow p-6">
                    <h3 className="text-lg font-semibold mb-4">Quick Insights</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                            <div className="flex items-center">
                                <span className="text-green-600 text-xl mr-2">📈</span>
                                <div>
                                    <div className="text-sm text-green-800 font-medium">
                                        Customer Satisfaction
                                    </div>
                                    <div className="text-lg font-bold text-green-600">
                                        {(((ratingDistribution[4] || 0) + (ratingDistribution[5] || 0)) / ratings.length * 100).toFixed(1)}%
                                    </div>
                                    <div className="text-xs text-green-600">4+ star reviews</div>
                                </div>
                            </div>
                        </div>

                        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                            <div className="flex items-center">
                                <span className="text-yellow-600 text-xl mr-2">💬</span>
                                <div>
                                    <div className="text-sm text-yellow-800 font-medium">
                                        Reviews with Comments
                                    </div>
                                    <div className="text-lg font-bold text-yellow-600">
                                        {ratings.filter(r => r.comment).length}
                                    </div>
                                    <div className="text-xs text-yellow-600">
                                        {(ratings.filter(r => r.comment).length / ratings.length * 100).toFixed(1)}% detailed
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                            <div className="flex items-center">
                                <span className="text-blue-600 text-xl mr-2">📊</span>
                                <div>
                                    <div className="text-sm text-blue-800 font-medium">
                                        Service Quality Score
                                    </div>
                                    <div className="text-lg font-bold text-blue-600">
                                        {(averageRating / 5 * 100).toFixed(0)}%
                                    </div>
                                    <div className="text-xs text-blue-600">Based on avg rating</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default RatingsPage;