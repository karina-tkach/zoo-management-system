import {useSearchParams, Link, useNavigate} from 'react-router-dom';
import { useEffect, useState } from 'react';
import {
    CheckCircle,
    Calendar,
    CreditCard,
    User,
    Ticket,
    LucideMail
} from 'lucide-react';
import {fetchData} from "../utils/fetch.js";
import {useLoading} from "../utils/useLoading.jsx";
import LoadingPage from "./LoadingPage.jsx";

const SuccessPage = () => {
    const [searchParams] = useSearchParams();
    const session_id = searchParams.get('session_id');
    const navigate = useNavigate();
    const { loading, start, stop } = useLoading();
    const [sessionDetails, setSessionDetails] = useState(null);

    useEffect(() => {
        if (!session_id) return;

        const fetchSessionDetails = async () => {
            await fetchData({
                url: `/api/stripe/get-session?session_id=${session_id}`,
                onSuccess: (data) => setSessionDetails(data),
                errorMessage: "Failed to load session data",
                navigate,
                onStart: start,
                onFinally: stop
            });
        };

        fetchSessionDetails();
    }, [session_id, navigate]);

    if (loading) {
        return <LoadingPage/>;
    }

    if (!sessionDetails) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-muted">
                <p className="text-lg text-destructive">Unable to retrieve ticket information.</p>
            </div>
        );
    }

    const { metadata, amount_total } = sessionDetails;

    return (
        <div className="min-h-screen bg-background flex items-center justify-center p-4">
            <div className="bg-white p-6 rounded-2xl shadow-xl max-w-md w-full border border-muted">
                <div className="flex items-center justify-center mb-4 text-green-600">
                    <CheckCircle className="w-10 h-10" />
                </div>
                <h1 className="text-2xl font-bold text-center mb-2">Payment Successful</h1>
                <p className="text-muted-foreground text-center mb-6">Thank you for your visit to the zoo! <br/>You will receive a ticket to the provided email.</p>

                <div className="space-y-3 text-lg text-gray-700">
                    <div className="flex items-center gap-2">
                        <User className="text-blue-600 w-5 h-5"/>
                        <span><strong>Name:</strong> {metadata.fullName}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <LucideMail className="text-blue-600 w-5 h-5"/>
                        <span><strong>Email:</strong> {metadata.email}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <Ticket className="text-violet-600 w-5 h-5"/>
                        <span><strong>Ticket Type:</strong> {metadata.ticketType}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <Ticket className="text-violet-600 w-5 h-5"/>
                        <span><strong>Visit Type:</strong> {metadata.visitType}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <Calendar className="text-green-600 w-5 h-5"/>
                        <span><strong>Visit Date:</strong> {metadata.visitDate}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <CreditCard className="text-yellow-600 w-5 h-5"/>
                        <span><strong>Paid:</strong> ₴{(amount_total / 100).toFixed(2)}</span>
                    </div>
                </div>

                <Link
                    to="/"
                    className="mt-6 inline-flex items-center justify-center gap-2 bg-green-600 text-white px-4 py-2 rounded-xl hover:bg-green-600/90 transition-colors w-full"
                >
                    Back to Home
                </Link>
            </div>
        </div>
    );
};

export default SuccessPage;
