import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import {useAuth} from "../context/AuthContext.jsx";

export default function Ticket() {
    const [ticket, setTicket] = useState([]);
    const { id } = useParams();
    const navigate = useNavigate();
    const { user, loading } = useAuth();

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

    useEffect(() => {
        const fetchTicket = async () => {
            try {
                const response = await fetch(`/api/tickets/${id}`, {
                    credentials: "include",
                });

                if (response.ok) {
                    const data = await response.json();
                    setTicket(data || []);
                } else {
                    const resData = await response.json();
                    navigate("/error", {
                        state: {
                            message: resData.message || "Failed to load ticket data",
                            code: response.status,
                        },
                    });
                }
            } catch {
                navigate("/error", {
                    state: {
                        message: "An unexpected error occurred",
                        code: 500,
                    },
                });
            }
        };

        fetchTicket();
    }, []);

    if (loading) {
        return (
            <div className="relative p-6 min-h-screen bg-green-50">
                <div className="absolute inset-0 bg-white/80 backdrop-blur-md flex items-center justify-center z-50">
                    <div className="text-center">
                        <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-green-600 border-solid mx-auto mb-4" />
                        <p className="text-2xl font-semibold text-green-700">Loading...</p>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-100 py-5 px-4 flex justify-center">
            <div className="bg-white text-black w-[360px] p-6 border border-gray-300 shadow-lg font-mono">
                <div className="text-center">
                    <h1 className="text-xl font-bold tracking-wide">Zoo Park</h1>
                </div>

                <hr className="my-4"/>

                <p className="text-center text-sm text-gray-700">Ticket #{ticket.id}</p>

                <hr className="mb-4"/>

                <div className="flex justify-between text-sm font-bold mb-2">
                    <span>Item</span>
                    <span>Price</span>
                </div>
                <div className="flex justify-between text-sm mb-4">
                    <span>{ticket.ticketType} Ticket</span>
                    <span>{(ticket.price)}</span>
                </div>
                <hr className="my-4"/>

                <div className="text-sm mb-2">
                    <p className="font-bold">UUID: {ticket.uuid}</p>
                    <p>Full Name: {ticket.fullName}</p>
                    <p>Visit Type: {ticket.visitType}</p>
                    {ticket.excursionName && <p>Excursion topic: {ticket.excursionName}</p>}
                    {ticket.excursionId && <p>Excursion id: {ticket.excursionId}</p>}
                    <p>Visit Date: {ticket.visitDate}</p>
                </div>

                <hr className="my-4"/>

                <div className="text-sm">
                    <p>Payment: {ticket.purchaseMethod}</p>
                </div>

                <div className="my-6 text-center">
                    <div className="flex justify-center gap-[2px] my-6">
                        {Array.from({length: 40}).map((_, i) => (
                            <div
                                key={i}
                                className={`h-16 ${
                                    i % 7 === 0
                                        ? "w-[2px] bg-black"
                                        : i % 5 === 0
                                            ? "w-[1px] bg-gray-800"
                                            : "w-[1px] bg-black"
                                }`}
                            />
                        ))}
                    </div>
                    {/* Barcode placeholder */}
                </div>

                <p className="text-xs text-center mt-4 text-gray-500">
                    Purchased on {new Date(ticket.purchaseTime).toLocaleString()}
                </p>

                <button
                    onClick={() => navigate("/tickets")}
                    className="mt-6 w-full bg-gray-200 hover:bg-gray-300 text-gray-700 text-sm py-2 rounded"
                >
                    <ArrowLeft className="inline w-4 h-4 mr-1"/> Back to Tickets
                </button>
            </div>
        </div>
    );
}
