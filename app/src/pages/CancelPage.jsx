import { Link } from 'react-router-dom';
import { Frown } from 'lucide-react';

function CancelPage() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-yellow-100 px-4">
            <div className="bg-white rounded-2xl shadow-xl p-8 max-w-md w-full text-center border-2 border-yellow-400">
                <Frown className="w-16 h-16 text-yellow-500 mx-auto mb-4" />
                <h1 className="text-3xl font-bold text-yellow-700 mb-2">Payment Canceled</h1>
                <p className="text-gray-700 mb-6">
                    Unfortunately, your payment was canceled. But don’t worry — you can always go back and book your ticket again!
                </p>
                <Link
                    to="/"
                    className="inline-block bg-yellow-500 hover:bg-yellow-600 text-white font-semibold py-2 px-4 rounded-xl transition"
                >
                    Return to Home
                </Link>
            </div>
        </div>
    );
}

export default CancelPage;
