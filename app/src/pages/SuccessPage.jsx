import { useSearchParams, Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import {
    CheckCircle,
    Calendar,
    CreditCard,
    User,
    Ticket,
} from 'lucide-react';

const SuccessPage = () => {
    const [searchParams] = useSearchParams();
    const session_id = searchParams.get('session_id');

    const [loading, setLoading] = useState(true);
    const [sessionDetails, setSessionDetails] = useState(null);

    useEffect(() => {
        if (!session_id) return;

        const fetchSessionDetails = async () => {
            try {
                const res = await fetch(`/api/stripe/get-session?session_id=${session_id}`);
                if (!res.ok) throw new Error('Failed to load session data.');
                const data = await res.json();
                setSessionDetails(data);
            } catch (err) {
                console.error(err);
                setSessionDetails(null);
            } finally {
                setLoading(false);
            }
        };

        fetchSessionDetails();
    }, [session_id]);

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-muted">
                <p className="text-lg text-muted-foreground">Loading...</p>
            </div>
        );
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
            <div className="bg-white p-6 md:p-10 rounded-2xl shadow-xl max-w-md w-full border border-muted">
                <div className="flex items-center justify-center mb-4 text-green-600">
                    <CheckCircle className="w-10 h-10" />
                </div>
                <h1 className="text-2xl font-bold text-center mb-2">Payment Successful</h1>
                <p className="text-muted-foreground text-center mb-6">Thank you for your visit to the zoo!</p>

                <div className="space-y-3 text-sm text-gray-700">
                    <div className="flex items-center gap-2">
                        <User className="text-blue-600 w-4 h-4" />
                        <span><strong>Name:</strong> {metadata.fullName}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <Ticket className="text-violet-600 w-4 h-4" />
                        <span><strong>Ticket Type:</strong> {metadata.ticketType}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <Calendar className="text-green-600 w-4 h-4" />
                        <span><strong>Visit Date:</strong> {metadata.visitDate}</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <CreditCard className="text-yellow-600 w-4 h-4" />
                        <span><strong>Paid:</strong> ${(amount_total / 100).toFixed(2)}</span>
                    </div>
                </div>

                <Link
                    to="/"
                    className="mt-6 inline-flex items-center justify-center gap-2 bg-primary text-white px-4 py-2 rounded-xl hover:bg-primary/90 transition-colors w-full"
                >
                    Back to Home
                </Link>
            </div>
        </div>
    );
};

export default SuccessPage;
