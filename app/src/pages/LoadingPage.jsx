import React from "react";

export default function LoadingPage() {
    return (
        <div className="relative p-6 min-h-screen bg-gray-200">
            <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                <div className="text-center">
                    <div
                        className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 border-solid mx-auto mb-4"/>
                    <p className="text-xl font-semibold text-gray-700">Loading...</p>
                </div>
            </div>
        </div>
    );
}