import { useAuth } from "../context/AuthContext";
import {Navigate} from "react-router-dom";
import React from "react";
import LoadingPage from "../pages/LoadingPage.jsx";

const ProtectedRoute = ({ children, requiredRoles }) => {
    const { user, loading } = useAuth();

    if (loading)
        return (
        <LoadingPage/>
    );

    if (!user || !requiredRoles.some(role => user?.roles.includes(role))) {
        return (
            <Navigate
                to="/error"
                replace
                state={{
                    message: "Access Denied",
                    code: 403,
                    from: location.pathname,
                }}

            />
        );
    }

    return children;
};

export default ProtectedRoute;
