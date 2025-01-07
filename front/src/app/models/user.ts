export type User = {
    id: number,
    name: string;
    surname: string;
    email: string;
    password: string;
    permission: {
        create:boolean,
        update:boolean,
        delete:boolean,
        read:boolean,
    }
}