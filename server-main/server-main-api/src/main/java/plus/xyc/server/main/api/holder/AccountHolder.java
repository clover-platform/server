package plus.xyc.server.main.api.holder;

import plus.xyc.server.main.api.entity.response.ApiAccountResponse;

public class AccountHolder {
    
    private static final ThreadLocal<ApiAccountResponse> holder = new ThreadLocal<>();

    public static void set(ApiAccountResponse account) {
        holder.set(account);
    }

    public static ApiAccountResponse get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }

}
