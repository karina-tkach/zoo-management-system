import { Link } from 'react-router-dom';
import { AlertTriangle } from 'lucide-react';

export default function NotFound() {
    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-green-100 px-4">
            <div className="w-full max-w-lg bg-white shadow-xl rounded-2xl p-8 text-center">
                <div className="flex justify-center mb-6 text-red-500">
                    <AlertTriangle className="h-12 w-12" />
                </div>
                <h1 className="text-5xl font-extrabold text-gray-800 mb-2">404</h1>
                <h2 className="text-xl font-semibold text-gray-600 mb-4">Page Not Found</h2>
                <p className="text-gray-500 mb-6">
                    Sorry, the page you're looking for doesn't exist or has been moved.
                </p>
                <Link
                    to="/"
                    className="inline-block bg-green-800 text-white font-medium px-5 py-2.5 rounded-lg shadow hover:bg-green-700 transition"
                >
                    ⬅ Go back to Home
                </Link>
            </div>
        </div>
    );
}
