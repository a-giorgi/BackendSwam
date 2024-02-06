export default interface FeedDTO {
    id: number | null;
    active: boolean;
    countryCode: string;
    name: string;
    tags: string;
    topNews: boolean;
    url: string;
}
