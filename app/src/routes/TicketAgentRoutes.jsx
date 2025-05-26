import { Route } from "react-router-dom";
import TicketPricings from "../pages/TicketPricings.jsx";
import TicketsPage from "../pages/TicketsPage.jsx";
import Ticket from "../pages/Ticket.jsx";
import TicketAddForm from "../pages/TicketAddForm.jsx";
import GatesPage from "../pages/GatesPage.jsx";
import GatesForm from "../pages/GatesForm.jsx";
import VisitsPage from "../pages/VisitsPage.jsx";
import VisitsForm from "../pages/VisitsForm.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";

const TicketAgentRoutes = [
        <Route path="/pricings" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <TicketPricings/>
        </ProtectedRoute>} />,

        <Route path="/tickets" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <TicketsPage/>
        </ProtectedRoute>} />,
        <Route path="/tickets/:id" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <Ticket/>
        </ProtectedRoute>} />,
        <Route path="/tickets/add" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <TicketAddForm/>
        </ProtectedRoute>} />,


        <Route path="/gates" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <GatesPage/>
        </ProtectedRoute>} />,
        <Route path="/gates/add" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <GatesForm/>
        </ProtectedRoute>} />,
        <Route path="/gates/edit/:id" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <GatesForm/>
        </ProtectedRoute>} />,

        <Route path="/visits" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <VisitsPage/>
        </ProtectedRoute>} />,
        <Route path="/visits/add" element={<ProtectedRoute requiredRoles={["TICKET_AGENT"]}>
            <VisitsForm/>
        </ProtectedRoute>} />
    ];

export default TicketAgentRoutes;
