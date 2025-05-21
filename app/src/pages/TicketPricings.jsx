import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from '../context/AuthContext';

export default function TicketPricings() {
    const [pricings, setPricings] = useState([]);
    const navigate = useNavigate();
    const { user, loading } = useAuth();
    const [editingId, setEditingId] = useState(null);
    const [newPrice, setNewPrice] = useState("");
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        if (!loading && !user?.roles.includes("TICKET_AGENT")) {
            navigate("/error", {
                state: {
                    message: "Access Denied: Ticket agents only",
                    code: 403,
                },
            });
        }
    }, [user, loading, navigate]);

    const fetchPricings = async () => {
        try {
            const response = await fetch(`/api/ticket-pricings`, {credentials: "include"});

            if (response.status === 200) {
                const pricings = await response.json();
                setPricings(pricings);
            } else {
                const resData = await response.json();
                navigate('/error', {
                    state: {
                        message: resData.message || 'Failed to load staff data',
                        code: response.status
                    }
                });
            }
        } catch (error) {
            navigate('/error', {
                state: {
                    message: 'An unexpected error occurred',
                    code: 500
                }
            });
        }
    };

    useEffect(() => {
        fetchPricings();
    }, []);

    const handleUpdateClick = (id) => {
        setEditingId(id);
        setShowModal(true);
    };

    const handleSave = async () => {
        if (isNaN(newPrice) || newPrice < 0) {
            alert("Price must be a non-negative number");
            return;
        }

        try {
            const response = await fetch(`/api/ticket-pricings/${editingId}?price=${newPrice}`, {
                method: "PATCH",
                credentials: "include",
            });

            if (response.ok) {
                await fetchPricings();
                setShowModal(false);
                setNewPrice("");
                setEditingId(null);
            } else {
                const resData = await response.json();
                navigate('/error', {
                    state: {
                        message: resData.message || 'Failed to update pricing',
                        code: response.status
                    }
                });
            }
        } catch (err) {
            navigate('/error', {
                state: {
                    message: 'An unexpected error occurred',
                    code: 500
                }
            });
        }
    };

    if (loading)
        return (
            <div className="relative p-6 min-h-screen bg-gray-200">

                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-blue-600 border-solid mx-auto mb-4" />
                        <p className="text-xl font-semibold text-gray-700">Loading...</p>
                    </div>
                </div>
            </div>
        );

    return (
        <>
        <div className="w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">Pricings List</h2>
            </div>

            <div className="overflow-x-auto border border-gray-200 rounded-md shadow-sm">
                <table className="min-w-[1000px] divide-y divide-gray-200 w-full">
                    <thead className="bg-gray-50">
                    <tr className="divide-x divide-gray-200">
                        {[
                            "Ticket type",
                            "Visit type",
                            "Price",
                            "Actions",
                        ].map((header) => (
                            <th
                                key={header}
                                className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                            >
                                {header}
                            </th>
                        ))}
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {pricings.length === 0 ? (
                        <tr>
                            <td
                                colSpan={8}
                                className="text-center py-4 text-gray-500 italic"
                            >
                                No pricings found
                            </td>
                        </tr>
                    ) : (
                        pricings.map((s) => (
                            <tr key={s.id} className="hover:bg-gray-50 divide-x divide-gray-200">
                                <td className="px-4 py-3 whitespace-nowrap text-gray-900">
                                    {s.ticketType}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.visitType}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap text-gray-700">
                                    {s.price}
                                </td>
                                <td className="px-4 py-3 whitespace-nowrap space-x-2">
                                    <button
                                        onClick={() => handleUpdateClick(s.id)}
                                        className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded-md text-sm font-semibold"
                                    >
                                        Update price
                                    </button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    {showModal && (
            <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                <div className="bg-white rounded-lg p-6 w-full max-w-sm shadow-lg">
                    <h3 className="text-lg font-semibold mb-4">Enter new price</h3>
                    <input
                        type="number"
                        value={newPrice}
                        onChange={(e) => setNewPrice(e.target.value)}
                        className="w-full px-4 py-2 mb-4 border rounded-md"
                        placeholder="Enter price"
                    />
                    <div className="flex justify-end space-x-2">
                        <button
                            onClick={() => setShowModal(false)}
                            className="px-4 py-2 bg-gray-300 rounded-md hover:bg-gray-400"
                        >
                            Cancel
                        </button>
                        <button
                            onClick={handleSave}
                            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                        >
                            Save
                        </button>
                    </div>
                </div>
            </div>)}
            </>);
}
