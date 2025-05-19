import { useLocation, useNavigate } from "react-router-dom";
import { AlertCircle } from "lucide-react";

export default function ErrorPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const { message, code } = location.state || {
        message: "An unexpected error occurred",
        code: 500,
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-red-100 px-4">
            <div className="w-full max-w-lg bg-white shadow-xl rounded-2xl p-8 text-center">
                <div className="flex justify-center mb-6 text-red-600">
                    <AlertCircle className="h-12 w-12" />
                </div>
                <h1 className="text-5xl font-extrabold text-red-700 mb-2">Error {code}</h1>
                <p className="text-gray-600 mb-6">{message}</p>
                <button
                    onClick={() => navigate(-1)}
                    className="inline-block bg-blue-600 text-white font-medium px-6 py-2.5 rounded-lg shadow hover:bg-blue-700 transition"
                >
                    ← Go Back
                </button>
            </div>
        </div>
    );
}
