import React, {useState} from "react";

interface RatingFormProps {
    onSubmit: (rating: number, comment: string) => void;
}

interface RatingFormProps {
    onSubmit: (rating: number, comment: string) => void;
}

const RatingForm: React.FC<RatingFormProps> = ({onSubmit}) => {
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState('');

    const stars = [1, 2, 3, 4, 5];

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (rating === 0) return alert('Please select a rating');
        onSubmit(rating, comment);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <div className="flex space-x-1 text-yellow-400">
                {stars.map(star => (
                    <button
                        key={star}
                        type="button"
                        onClick={() => setRating(star)}
                        className={star <= rating ? 'text-yellow-400' : 'text-gray-300'}
                    >
                        ★
                    </button>
                ))}
            </div>
            <textarea
                placeholder="Leave a comment (optional)"
                value={comment}
                onChange={e => setComment(e.target.value)}
                className="w-full border rounded p-2"
            />
            <button
                type="submit"
                className="bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition"
            >
                Submit Rating
            </button>
        </form>
    );
};

export default RatingForm;

