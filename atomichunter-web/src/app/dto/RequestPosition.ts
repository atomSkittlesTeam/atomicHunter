import { Product } from "./Product";

export class RequestPosition {
    id: number;
    requestId: number;
    note: string;
    archive: boolean;
    product: Product;
}