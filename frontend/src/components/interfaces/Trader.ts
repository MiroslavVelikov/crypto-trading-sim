type UUID = string;

export default interface Trader {
    id: UUID;
    username: string;
    fullName: string;
    walletId: UUID;
}