import { useEffect } from 'react';
import { TRADER_ID_TOKEN, TRADER_NAME_COOKIE, TRADER_USERNAME_COOKIE } from '../../common/utils/utils';

const Logout = () => {
    useEffect(() => {
        document.cookie = TRADER_ID_TOKEN + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        document.cookie = TRADER_NAME_COOKIE + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        document.cookie = TRADER_USERNAME_COOKIE + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        
        window.location.href = '/login';
    }, []);

    return <div></div>;
}

export default Logout;