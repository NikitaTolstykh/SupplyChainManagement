import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import Button from "../../components/ui/Button.tsx";
import { useRateOrder } from "../../hooks/client/useRating.ts";

const RatingPage: React.FC = () => {
    const { orderId } = useParams<{ orderId: string }>();
    const id = orderId ? parseInt(orderId) : 0;

    const [rating, setRating] = useState<number>(0);
    const [comment, setComment] = useState<string>("");

    const { mutate, isPending, isError, isSuccess } = useRateOrder();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (rating < 1 || rating > 5) {
            alert("Please provide a rating between 1 and 5.");
            return;
        }
        mutate({ orderId: id, rating: { stars: rating, comment } });
    };

    const StarRating = ({
                            rating,
                            onRatingChange
                        }: {
        rating: number;
        onRatingChange: (rating: number) => void
    }) => {
        return (
            <div className="flex space-x-1">
                {[1, 2, 3, 4, 5].map((star) => (
                    <button
                        key={star}
                        type="button"
                        onClick={() => onRatingChange(star)}
                        className={`text-3xl transition-all duration-200 hover:scale-110 transform ${
                            star <= rating ? 'text-yellow-400' : 'text-gray-300'
                        } hover:text-yellow-400`}
                    >
                        ★
                    </button>
                ))}
            </div>
        );
    };

    if (isSuccess) {
        return (
            <div className="max-w-lg mx-auto text-center py-8">
                <div className="bg-green-50 border border-green-200 rounded-lg p-6">
                    <div className="text-green-800 mb-4">
                        <svg className="w-16 h-16 mx-auto mb-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                        </svg>
                        <h2 className="text-xl font-semibold mb-2">Thank you for your feedback!</h2>
                        <p>Rating submitted successfully!</p>
                    </div>
                    <div className="space-x-4">
                        <Link
                            to="/client/orders"
                            className="inline-block bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md transition-colors"
                        >
                            Back to Orders
                        </Link>
                        <Link
                            to={`/client/orders/${id}`}
                            className="inline-block bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md transition-colors"
                        >
                            View Order Details
                        </Link>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="max-w-lg mx-auto">
            <div className="flex items-center justify-between mb-6">
                <h1 className="text-2xl font-semibold">Rate Order #{id}</h1>
                <Link to="/client/orders" className="text-blue-600 hover:text-blue-800">
                    ← Back to Orders
                </Link>
            </div>

            <div className="bg-white rounded-lg shadow p-6">
                <form onSubmit={handleSubmit} className="space-y-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-3">
                            How would you rate this service?
                        </label>
                        <StarRating rating={rating} onRatingChange={setRating} />
                        <p className="text-sm text-gray-500 mt-2">
                            Click on the stars to rate your experience (1-5 stars)
                        </p>
                        {rating > 0 && (
                            <p className="text-sm text-blue-600 mt-1">
                                You selected: {rating} star{rating !== 1 ? 's' : ''}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Tell us about your experience (optional):
                        </label>
                        <textarea
                            rows={4}
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                            className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="Share details about your experience with this delivery..."
                        />
                    </div>

                    {isError && (
                        <div className="bg-red-50 border border-red-200 rounded-md p-4">
                            <div className="text-red-800 text-sm">
                                Failed to submit rating. Please try again.
                            </div>
                        </div>
                    )}

                    <div className="flex justify-end space-x-3">
                        <Link
                            to="/client/orders"
                            className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
                        >
                            Cancel
                        </Link>
                        <Button
                            type="submit"
                            variant="primary"
                            disabled={isPending || rating === 0}
                        >
                            {isPending ? "Submitting..." : "Submit Rating"}
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RatingPage;